package com.example.parkpal.presentation

import android.location.Location
import kotlinx.coroutines.flow.Flow

interface UserLocation {
    fun getLocationUpdates(interval: Long) : Flow<Location>

    class LocationException(message: String) : Exception()
}