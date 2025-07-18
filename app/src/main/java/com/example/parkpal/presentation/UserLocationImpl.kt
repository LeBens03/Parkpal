package com.example.parkpal.presentation

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.os.Looper
import android.util.Log
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.Priority
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class UserLocationImpl @Inject constructor(
    private val context: Context,
    private val fusedLocationClient: FusedLocationProviderClient
) : UserLocation {

    private fun Context.hasLocationPermission(): Boolean {
        val fineLocation = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
        val coarseLocation = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
        return fineLocation && coarseLocation
    }


    @SuppressLint("MissingPermission")
    override fun getLocationUpdates(interval: Long): Flow<Location> {
        return callbackFlow {
            Log.d("UserLocationImpl", "getLocationUpdates started")

            // Check permissions
            if (!context.hasLocationPermission()) {
                Log.e("UserLocationImpl", "Missing location permission")
                close(UserLocation.LocationException("Missing location permission"))
                return@callbackFlow
            }

            // Check GPS/Network availability
            val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as android.location.LocationManager
            val isGpsEnabled = locationManager.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER)
            val isNetworkEnabled = locationManager.isProviderEnabled(android.location.LocationManager.NETWORK_PROVIDER)

            Log.d("UserLocationImpl", "GPS enabled: $isGpsEnabled, Network enabled: $isNetworkEnabled")

            if (!isGpsEnabled && !isNetworkEnabled) {
                Log.e("UserLocationImpl", "GPS and Network are disabled")
                close(UserLocation.LocationException("GPS is disabled"))
                return@callbackFlow
            }

            // Create LocationRequest
            val locationRequest = LocationRequest.Builder(interval)
                .setPriority(Priority.PRIORITY_HIGH_ACCURACY)
                .build()

            // LocationCallback
            val locationCallback = object : LocationCallback() {
                override fun onLocationResult(result: LocationResult) {
                    result.locations.lastOrNull()?.let { location ->
                        trySend(location).isSuccess // Emit location safely
                    } ?: Log.e("UserLocationImpl", "No location available in result")
                }
            }

            // Start Location Updates
            fusedLocationClient.requestLocationUpdates(
                locationRequest,
                locationCallback,
                Looper.getMainLooper()
            )

            // Stop updates on flow close
            awaitClose {
                Log.d("UserLocationImpl", "Stopping location updates")
                fusedLocationClient.removeLocationUpdates(locationCallback)
            }
        }
    }
}