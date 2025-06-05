package com.example.parkpal.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.parkpal.data.model.User
import com.example.parkpal.data.repository.UserRepository
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class UserViewModel(private val repository: UserRepository) : ViewModel() {
    val users: StateFlow<List<User>> = repository.users

    fun getAllUsers() {
        viewModelScope.launch {
            repository.getAllUsers()
        }
    }

    fun saveUnregisteredUser(user: User) {
        // Launch a coroutine to save data in a background thread
        viewModelScope.launch {
            repository.insertUser(user)
        }
    }
}