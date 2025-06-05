package com.example.parkpal.data.repository

import com.example.parkpal.data.dao.UserDAO
import com.example.parkpal.data.model.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class UserRepository(private val userDao: UserDAO) {
    // MutableStateFlow to hold the current list of users
    private val _users = MutableStateFlow<List<User>>(emptyList())
    val users: StateFlow<List<User>> = _users.asStateFlow()
    private val viewModelScope = CoroutineScope(Dispatchers.IO)

    // Fetch all users from the DAO and update the StateFlow
    init {
        viewModelScope.launch {
            userDao.getAllUsers().collect { userList ->
                _users.value = userList
            }
        }
    }

    // Insert a new user and refresh the user list
    suspend fun insertUser(user: User) {
        userDao.insertUser(user)
    }

    suspend fun getAllUsers() {
        userDao.getAllUsers()
    }
}
