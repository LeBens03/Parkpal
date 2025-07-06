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

/**
 * ViewModel class responsible for handling user-related UI state and actions.
 *
 * Interacts with the [UserRepository] to perform operations such as fetching,
 * inserting, and updating user data. Maintains a [MutableLiveData] for the current user.
 *
 * This ViewModel is scoped to Hilt for dependency injection.
 *
 * @param userRepository The repository used to access user-related data.
 */
@HiltViewModel
class UserViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    /** Holds the currently authenticated user, or null if none is set. */
    private val _currentUser = MutableLiveData<User?>(null)
    val currentUser: MutableLiveData<User?> = _currentUser

    /**
     * Fetches the current user using the authenticated Firebase user's email.
     *
     * If the user is not authenticated, an [IllegalStateException] is thrown.
     * Updates [currentUser] LiveData on success.
     */
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

    /**
     * Inserts a new user into the database and updates [currentUser] with the result.
     *
     * @param user The [User] object to insert.
     */
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

    /**
     * Updates the given user in the database and sets it as the [currentUser].
     *
     * @param user The [User] object with updated information.
     */
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

    /**
     * Clears the [currentUser] LiveData by setting it to null.
     */
    fun clearCurrentUser() {
        Log.d("UserViewModel", "Clearing current user")
        currentUser.value = null
    }

    /**
     * Manually sets the [currentUser] LiveData to an existing user.
     *
     * @param existingUser The user to set as current.
     */
    fun setCurrentUser(existingUser: User) {
        currentUser.value = existingUser
    }
}