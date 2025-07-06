package com.example.parkpal.presentation.viewmodel

import android.content.Context
import android.location.Geocoder
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.parkpal.R
import com.example.parkpal.domain.model.ParkingLocation
import com.example.parkpal.domain.repository.ParkingLocationRepository
import com.example.parkpal.presentation.utils.MapEvent
import com.example.parkpal.presentation.utils.MapState
import com.example.parkpal.presentation.utils.UserLocationImpl
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Locale
import javax.inject.Inject

/**
 * ViewModel that handles map interactions, parking logic, geolocation updates, and UI state management.
 *
 * @property context Application context used for accessing resources like map style.
 * @property parkingLocationRepository Repository for inserting parking location data.
 * @property userLocationImpl Service providing real-time user location updates.
 */
@HiltViewModel
class MapViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val parkingLocationRepository: ParkingLocationRepository,
    private val userLocationImpl: UserLocationImpl
) : ViewModel() {

    // Compose state for UI
    private var _state by mutableStateOf(MapState())
    val state: MapState get() = _state

    // Address state flow resolved from coordinates
    private val _address = MutableStateFlow("Loading...")
    val address: StateFlow<String> get() = _address

    // Distance (in kilometers) between user and parking location
    private val _distance = MutableStateFlow<Float?>(null)
    val distance: StateFlow<Float?> get() = _distance

    init {
        loadMapStyle()
    }

    /**
     * Safely updates the map state using a lambda.
     */
    private fun updateState(update: MapState.() -> MapState) {
        _state = _state.update()
    }

    /**
     * Loads a custom map style from a raw resource and applies it to the state.
     */
    private fun loadMapStyle() {
        try {
            val mapStyle = MapStyleOptions.loadRawResourceStyle(context, R.raw.map_style)
            updateState {
                copy(properties = properties.copy(mapStyleOptions = mapStyle))
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * Begins collecting live location updates for the user and updates the state accordingly.
     */
    fun startLocationUpdates() {
        viewModelScope.launch {
            userLocationImpl.getLocationUpdates(interval = 5000L).collect { location ->
                updateState {
                    copy(userLocation = LatLng(location.latitude, location.longitude))
                }
            }
        }
    }

    /**
     * Converts parking location coordinates to a human-readable address using the [Geocoder].
     *
     * @param context Context used to initialize the geocoder.
     * @param parkingLocation Parking location whose address is to be resolved.
     */
    fun fetchAddress(context: Context, parkingLocation: ParkingLocation?) {
        viewModelScope.launch {
            val resolvedAddress = withContext(Dispatchers.IO) {
                if (parkingLocation == null) return@withContext "Unknown Address"

                try {
                    val geocoder = Geocoder(context, Locale.getDefault())
                    val addressList = geocoder.getFromLocation(
                        parkingLocation.latitude,
                        parkingLocation.longitude,
                        1
                    )
                    addressList?.firstOrNull()?.getAddressLine(0) ?: "Unknown Address"
                } catch (e: Exception) {
                    "Unknown Address"
                }
            }

            updateState {
                copy(parkingLocation = parkingLocation?.copy(address = resolvedAddress))
            }
            _address.value = resolvedAddress
        }
    }

    /**
     * Calculates the distance between the user's current location and a selected parking location.
     *
     * @param userLocation Current user coordinates.
     * @param parkingLocation Selected parking location.
     */
    fun calculateDistance(userLocation: LatLng?, parkingLocation: ParkingLocation?) {
        viewModelScope.launch {
            val calculatedDistance = if (userLocation == null || parkingLocation == null) {
                null
            } else {
                val results = FloatArray(1)
                android.location.Location.distanceBetween(
                    userLocation.latitude, userLocation.longitude,
                    parkingLocation.latitude, parkingLocation.longitude,
                    results
                )
                results[0] / 1000 // Convert to kilometers
            }
            _distance.value = calculatedDistance
        }
    }

    /**
     * Handles map events such as parking a car or selecting a location.
     *
     * @param event The map-related event to handle.
     */
    fun onEvent(event: MapEvent) {
        when (event) {
            is MapEvent.OnParkMyCarClicked -> {
                viewModelScope.launch {
                    try {
                        Log.d("MapViewModel", "Inserting parking location: ${event.parkingLocation}")
                        parkingLocationRepository.insertParkingLocation(event.parkingLocation)
                        updateState { copy(parkingLocation = event.parkingLocation) }
                    } catch (e: Exception) {
                        println("Error inserting parking location: ${e.message}")
                    }
                }
            }

            is MapEvent.OnParkingLocationClicked -> {
                viewModelScope.launch {
                    try {
                        Log.d("MapViewModel", "Updating parking location: ${event.parkingLocation}")
                        parkingLocationRepository.insertParkingLocation(event.parkingLocation)
                        updateState { copy(parkingLocation = null) }
                    } catch (e: Exception) {
                        println("Error updating parking location: ${e.message}")
                    }
                }
            }
        }
    }
}