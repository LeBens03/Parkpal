package com.example.parkpal.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.parkpal.domain.model.User
import com.example.parkpal.domain.repository.UserRepository
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _currentUser = MutableLiveData<User?>(null)
    val currentUser: MutableLiveData<User?> = _currentUser

    fun fetchCurrentUser() {
        viewModelScope.launch {
            try {
                val email = FirebaseAuth.getInstance().currentUser?.email
                    ?: throw IllegalStateException("User is not authenticated")

                Log.d("UserViewModel", "Current user email: $email")

                val user = userRepository.getUserByEmail(email)
                currentUser.value = user

            } catch (e: Exception) {
                Log.e("UserViewModel", "Failed to fetch current user", e)
            }
        }
    }

    fun insertUser(user: User) {
        viewModelScope.launch {
            try {
                val userId = userRepository.insertUser(user)
                currentUser.value = user.copy(userId = userId)
            } catch (e: Exception) {
                Log.e("UserViewModel", "Failed to insert user", e)
            }
        }
    }

    fun updateUser(user: User) {
        viewModelScope.launch {
            try {
                userRepository.updateUser(user)
                currentUser.value = user
            } catch (e: Exception) {
                Log.e("UserViewModel", "Failed to update user", e)
            }
        }
    }

    fun clearCurrentUser() {
        Log.d("UserViewModel", "Clearing current user")
        currentUser.value = null
    }

    fun setCurrentUser(existingUser: User) {
        currentUser.value = existingUser
    }
}