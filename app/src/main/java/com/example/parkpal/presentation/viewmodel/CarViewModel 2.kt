package com.example.parkpal.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.parkpal.domain.model.Car
import com.example.parkpal.domain.repository.CarRepository
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel responsible for managing car-related UI state and operations.
 *
 * Interacts with the [CarRepository] to handle all operations related to the user's cars,
 * including fetching, inserting, deleting, and resolving license plates.
 *
 * Uses [StateFlow] to expose immutable streams of data for UI observation.
 */
@HiltViewModel
class CarViewModel @Inject constructor(
    private val carRepository: CarRepository
) : ViewModel() {

    /** StateFlow holding the current user's list of cars. */
    private val _currentUserCars = MutableStateFlow<List<Car>>(emptyList())
    val currentUserCars: StateFlow<List<Car>> = _currentUserCars

    /** StateFlow mapping car IDs to their license plates. */
    private val _licensePlates = MutableStateFlow<Map<Long, String>>(emptyMap())
    val licensePlates: StateFlow<Map<Long, String>> = _licensePlates

    /**
     * Fetches the list of cars for the currently authenticated user (by email).
     *
     * Updates [_currentUserCars] on success.
     */
    fun fetchCarsOfCurrentUser() {
        viewModelScope.launch {
            try {
                val email = FirebaseAuth.getInstance().currentUser?.email
                    ?: throw IllegalStateException("User is not authenticated")

                Log.d("CarViewModel", "Current user email: $email")

                val cars = carRepository.getCarByUserEmail(email)
                _currentUserCars.value = cars

                Log.d("CarViewModel", "Successfully fetched ${cars.size} cars for user: $email")
            } catch (e: Exception) {
                Log.e("CarViewModel", "Failed to fetch cars of current user", e)
            }
        }
    }

    /**
     * Inserts a new car for the current user.
     *
     * Updates [_currentUserCars] to include the new car on success.
     *
     * @param car The [Car] object to insert.
     */
    fun insertCar(car: Car) {
        viewModelScope.launch {
            try {
                carRepository.insertCar(car)
                _currentUserCars.value = _currentUserCars.value + car
                Log.d("CarViewModel", "Car inserted successfully")
            } catch (e: Exception) {
                Log.e("CarViewModel", "Failed to insert car", e)
            }
        }
    }

    /**
     * Deletes a car from the current user's list.
     *
     * Updates [_currentUserCars] to remove the car on success.
     *
     * @param car The [Car] object to delete.
     */
    fun deleteCar(car: Car) {
        viewModelScope.launch {
            try {
                carRepository.deleteCar(car)
                _currentUserCars.value = _currentUserCars.value.filter { it.carId != car.carId }
                Log.d("CarViewModel", "Car deleted successfully")
            } catch (e: Exception) {
                Log.e("CarViewModel", "Failed to delete car", e)
            }
        }
    }

    /**
     * Retrieves all cars for a given user ID.
     *
     * @param userId The user ID to fetch cars for.
     */
    fun getCarByUserId(userId: Long) {
        viewModelScope.launch {
            val list = carRepository.getCarsByUserId(userId)
            _currentUserCars.value = list
        }
    }

    /**
     * Fetches the license plate numbers for a list of car IDs.
     *
     * Updates [_licensePlates] with a map of carId → license plate.
     * If a car is not found, "Unknown" is used as fallback.
     *
     * @param carIds The list of car IDs to resolve.
     */
    fun fetchLicensePlatesForCars(carIds: List<Long>) {
        viewModelScope.launch {
            try {
                val plates = carIds.associateWith { carId ->
                    carRepository.getCarById(carId)?.licensePlate ?: "Unknown"
                }
                _licensePlates.value = plates
                Log.d("CarViewModel", "Loaded license plates: $plates")
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    /**
     * Clears the list of the current user's cars.
     */
    fun clearCarOfCurrentUser() {
        _currentUserCars.value = emptyList()
        Log.d("CarViewModel", "Cleared current cars")
    }

    /**
     * Sets the current user's cars manually.
     *
     * @param cars The list of [Car] objects to set.
     */
    fun setCarOfCurrentUser(cars: List<Car>) {
        _currentUserCars.value = cars
    }
}