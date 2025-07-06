package com.example.parkpal.presentation.utils

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Looper
import android.util.Log
import androidx.core.content.ContextCompat
import com.google.android.gms.location.*
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

/**
 * Implementation of [UserLocation] using Google's FusedLocationProviderClient
 * to provide real-time location updates via a Flow.
 *
 * @property context Application context used to access system services.
 * @property fusedLocationClient Client that provides location updates.
 */
class UserLocationImpl @Inject constructor(
    private val context: Context,
    private val fusedLocationClient: FusedLocationProviderClient
) : UserLocation {

    /**
     * Checks whether the app has the required location permissions.
     *
     * @return `true` if both fine and coarse location permissions are granted, `false` otherwise.
     */
    private fun Context.hasLocationPermission(): Boolean {
        val fineLocation = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
        val coarseLocation = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
        return fineLocation && coarseLocation
    }

    /**
     * Starts emitting location updates at the specified interval.
     *
     * @param interval Time in milliseconds between location updates.
     * @return A [Flow] that emits [Location] objects.
     * @throws UserLocation.LocationException If location permissions are missing or location services are disabled.
     */
    @SuppressLint("MissingPermission") // Permissions are checked before requesting updates
    override fun getLocationUpdates(interval: Long): Flow<Location> {
        return callbackFlow {
            Log.d("UserLocationImpl", "getLocationUpdates started")

            if (!context.hasLocationPermission()) {
                Log.e("UserLocationImpl", "Missing location permission")
                close(UserLocation.LocationException("Missing location permission"))
                return@callbackFlow
            }

            val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            val isGpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
            val isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)

            Log.d("UserLocationImpl", "GPS enabled: $isGpsEnabled, Network enabled: $isNetworkEnabled")

            if (!isGpsEnabled && !isNetworkEnabled) {
                Log.e("UserLocationImpl", "GPS and Network are disabled")
                close(UserLocation.LocationException("Location services are disabled"))
                return@callbackFlow
            }

            // Build the location request
            val locationRequest = LocationRequest.Builder(interval)
                .setPriority(Priority.PRIORITY_HIGH_ACCURACY)
                .build()

            // Define callback to receive location updates
            val locationCallback = object : LocationCallback() {
                override fun onLocationResult(result: LocationResult) {
                    result.locations.lastOrNull()?.let { location ->
                        trySend(location).isSuccess
                    } ?: Log.e("UserLocationImpl", "No location available in result")
                }
            }

            // Start receiving location updates
            fusedLocationClient.requestLocationUpdates(
                locationRequest,
                locationCallback,
                Looper.getMainLooper()
            )

            // Clean up location updates when the flow is closed or cancelled
            awaitClose {
                Log.d("UserLocationImpl", "Stopping location updates")
                fusedLocationClient.removeLocationUpdates(locationCallback)
            }
        }
    }
}