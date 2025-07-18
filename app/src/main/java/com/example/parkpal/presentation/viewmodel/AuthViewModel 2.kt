package com.example.parkpal.presentation.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.parkpal.presentation.utils.GoogleSignInClient
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel responsible for managing authentication state and operations.
 * Supports email/password and Google authentication using Firebase.
 */
open class AuthViewModel : ViewModel() {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    protected val _authState = MutableStateFlow<AuthState>(AuthState.Unauthenticated)
    open val authState: MutableStateFlow<AuthState> = _authState

    init {
        checkAuthState()
    }

    /**
     * Checks whether the user is already authenticated and updates the state.
     */
    fun checkAuthState() {
        _authState.value = if (auth.currentUser != null) {
            AuthState.Authenticated
        } else {
            AuthState.Unauthenticated
        }
    }

    /**
     * Registers a new user with email and password.
     */
    fun signUp(email: String, password: String) {
        if (email.isEmpty() || password.isEmpty()) {
            _authState.value = AuthState.Error("Email or password cannot be empty")
            return
        }

        _authState.value = AuthState.Loading
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                _authState.value = if (task.isSuccessful) {
                    Log.d("AuthViewModel", "User signed up successfully")
                    AuthState.Authenticated
                } else {
                    AuthState.Error(task.exception?.message ?: "Unknown error")
                }
            }
    }

    /**
     * Signs in a user with email and password.
     * Can be overridden for additional logic (e.g., logging or biometric checks).
     */
    open fun signIn(email: String, password: String) {
        if (email.isEmpty() || password.isEmpty()) {
            _authState.value = AuthState.Error("Email or password cannot be empty")
            return
        }

        _authState.value = AuthState.Loading
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                _authState.value = if (task.isSuccessful) {
                    AuthState.Authenticated
                } else {
                    AuthState.Error(task.exception?.message ?: "Unknown error")
                }
            }
    }

    /**
     * Updates the current user's password after re-authenticating with the old password.
     *
     * @param oldPassword The user's current password.
     * @param newPassword The new password to set.
     * @param onSuccess Callback for success.
     * @param onError Callback with error message if update fails.
     */
    fun updatePassword(
        oldPassword: String,
        newPassword: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        val currentUser = auth.currentUser
        if (currentUser == null) {
            onError("No user is currently signed in")
            return
        }

        val email = currentUser.email
        if (email.isNullOrEmpty()) {
            onError("Email not found for the current user")
            return
        }

        val credential = EmailAuthProvider.getCredential(email, oldPassword)
        currentUser.reauthenticate(credential)
            .addOnCompleteListener { reauthTask ->
                if (reauthTask.isSuccessful) {
                    currentUser.updatePassword(newPassword)
                        .addOnCompleteListener { updateTask ->
                            if (updateTask.isSuccessful) {
                                onSuccess()
                            } else {
                                onError(updateTask.exception?.message ?: "Failed to update password")
                            }
                        }
                } else {
                    onError(reauthTask.exception?.message ?: "Old password is incorrect")
                }
            }
    }

    /**
     * Signs in the user using a Google account.
     * Can be overridden for testing or extended flows.
     */
    open fun signInWithGoogle(context: Context) {
        viewModelScope.launch {
            val googleSignIn = GoogleSignInClient(context)
            val result = googleSignIn.signIn()

            _authState.value = if (result.isSuccess) {
                AuthState.Authenticated
            } else {
                AuthState.Error(result.exceptionOrNull()?.message ?: "Google Sign-In Failed")
            }
        }
    }

    /**
     * Signs the user out of Firebase and Google (if applicable).
     */
    fun signOut(context: Context) {
        viewModelScope.launch {
            val googleSignIn = GoogleSignInClient(context)
            val result = googleSignIn.signOut()

            _authState.value = if (result.isSuccess) {
                AuthState.Unauthenticated
            } else {
                AuthState.Error(result.exceptionOrNull()?.message ?: "Sign-out failed")
            }
        }
    }
}

/**
 * Represents the current state of authentication.
 */
sealed class AuthState {
    object Authenticated : AuthState()
    object Unauthenticated : AuthState()
    object Loading : AuthState()
    data class Error(val message: String) : AuthState()
}