package com.example.parkpal.presentation.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.parkpal.presentation.GoogleSignInClient
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {

    private val auth : FirebaseAuth = FirebaseAuth.getInstance()

    private val _authState = MutableStateFlow<AuthState>(AuthState.Unauthenticated)
    val authState : MutableStateFlow<AuthState> = _authState

    init {
        checkAuthState()
    }

    fun checkAuthState() {
        if (auth.currentUser != null) {
            _authState.value = AuthState.Authenticated
        } else {
            _authState.value = AuthState.Unauthenticated
        }
    }

    fun signUp(email : String, password : String) {
        if (email.isEmpty() || password.isEmpty()) {
            _authState.value = AuthState.Error("Email or password cannot be empty")
            return
        }
        _authState.value = AuthState.Loading
        auth.createUserWithEmailAndPassword(email,password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _authState.value = AuthState.Authenticated
                    Log.d("AuthViewModel", "User signed up successfully")
                } else {
                    _authState.value = AuthState.Error(task.exception?.message ?: "Unknown error")
                }
            }
    }

    fun signIn(email : String, password : String) {
        if (email.isEmpty() || password.isEmpty()) {
            _authState.value = AuthState.Error("Email or password cannot be empty")
            return
        }
        _authState.value = AuthState.Loading
        auth.signInWithEmailAndPassword(email,password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _authState.value = AuthState.Authenticated
                } else {
                    _authState.value = AuthState.Error(task.exception?.message ?: "Unknown error")
                }
            }
    }

    fun updatePassword(oldPassword: String, newPassword: String, onSuccess: () -> Unit, onError: (String) -> Unit) {
        val currentUser = auth.currentUser
        if (currentUser != null) {
            val email = currentUser.email
            if (email.isNullOrEmpty()) {
                onError("Email not found for the current user")
                return
            }

            // Re-authenticate the user
            val credential = EmailAuthProvider.getCredential(email, oldPassword)
            currentUser.reauthenticate(credential)
                .addOnCompleteListener { reauthTask ->
                    if (reauthTask.isSuccessful) {
                        // Update the password
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
        } else {
            onError("No user is currently signed in")
        }
    }

    fun signInWithGoogle(context: Context) {
        viewModelScope.launch {
            val googleSignIn = GoogleSignInClient(context)
            val result = googleSignIn.signIn()

            if (result.isSuccess) {
                _authState.value = AuthState.Authenticated
            } else {
                _authState.value = AuthState.Error(result.exceptionOrNull()?.message ?: "Google Sign-In Failed")
            }
        }
    }

    fun signOut(context: Context) {
        viewModelScope.launch {
            val googleSignIn = GoogleSignInClient(context)
            val result = googleSignIn.signOut()

            if (result.isSuccess) {
                _authState.value = AuthState.Unauthenticated
            } else {
                _authState.value = AuthState.Error(result.exceptionOrNull()?.message ?: "Sign-out failed")
            }
        }
    }
}

sealed class AuthState() {
    object Authenticated : AuthState()
    object Unauthenticated : AuthState()
    object Loading : AuthState()
    data class Error(val message : String ) : AuthState()
}
