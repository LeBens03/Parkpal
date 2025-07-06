package com.example.parkpal.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.parkpal.domain.model.ParkingHistory
import com.example.parkpal.domain.model.ParkingLocation
import com.example.parkpal.domain.repository.ParkingHistoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel responsible for managing parking history state and operations.
 *
 * Interacts with [ParkingHistoryRepository] to retrieve, insert, and modify the
 * user's parking history data. Uses [StateFlow] to expose the current parking history
 * state to the UI.
 *
 * @param parkingHistoryRepository The repository handling parking history persistence.
 */
@HiltViewModel
class ParkingHistoryViewModel @Inject constructor(
    private val parkingHistoryRepository: ParkingHistoryRepository
) : ViewModel() {

    /** Holds the current parking history for a user, or null if none is set. */
    private val _currentParkingHistory = MutableStateFlow<ParkingHistory?>(null)
    val currentParkingHistory: StateFlow<ParkingHistory?> get() = _currentParkingHistory

    /**
     * Fetches the parking history for the given user ID and updates [_currentParkingHistory].
     *
     * @param userId The ID of the user whose history should be fetched.
     */
    fun fetchParkingHistory(userId: Long) {
        viewModelScope.launch {
            try {
                val history = parkingHistoryRepository.getParkingHistoryByUserId(userId)
                _currentParkingHistory.value = history
                Log.d("ParkingHistoryViewModel", "Fetched parking history: $history")
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    /**
     * Adds a new [ParkingLocation] to the current user's history.
     * If no history exists yet, a new one is created.
     *
     * @param parkingLocation The parking location to add.
     * @param userId The user ID to associate with the parking history.
     */
    fun addParkingLocation(parkingLocation: ParkingLocation, userId: Long) {
        viewModelScope.launch {
            try {
                Log.d("ParkingHistoryViewModel", "Adding parking location to history: $parkingLocation")

                val currentHistory = _currentParkingHistory.value
                val updatedParkingHistory = currentHistory?.copy(
                    parkingLocations = currentHistory.parkingLocations + parkingLocation
                ) ?: ParkingHistory(
                    userId = userId,
                    parkingLocations = listOf(parkingLocation)
                )

                parkingHistoryRepository.insertParkingHistory(updatedParkingHistory)
                _currentParkingHistory.value = updatedParkingHistory

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    /**
     * Removes a [ParkingLocation] from the current user's history.
     *
     * @param parkingLocation The parking location to remove.
     */
    fun deleteParkingLocation(parkingLocation: ParkingLocation) {
        viewModelScope.launch {
            try {
                Log.d("ParkingHistoryViewModel", "Deleting parking location: $parkingLocation")
                val currentHistory = _currentParkingHistory.value
                if (currentHistory != null) {
                    val updatedParkingHistory = currentHistory.copy(
                        parkingLocations = currentHistory.parkingLocations - parkingLocation
                    )
                    parkingHistoryRepository.insertParkingHistory(updatedParkingHistory)
                    _currentParkingHistory.value = updatedParkingHistory
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    /**
     * Deletes the parking history for a specific user and clears [_currentParkingHistory].
     *
     * @param userId The ID of the user whose parking history should be deleted.
     */
    fun clearParkingHistory(userId: Long) {
        viewModelScope.launch {
            try {
                val currentHistory = currentParkingHistory.value
                Log.d("ParkingHistoryViewModel", "Deleting parking history: $currentHistory")
                if (currentHistory != null) {
                    parkingHistoryRepository.deleteParkingHistoryById(userId)
                    _currentParkingHistory.value = null
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}