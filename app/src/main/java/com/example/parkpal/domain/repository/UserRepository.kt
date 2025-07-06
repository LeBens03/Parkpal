package com.example.parkpal.domain.repository

import android.util.Log
import com.example.parkpal.data.dao.UserDao
import com.example.parkpal.data.mapper.toUser
import com.example.parkpal.data.mapper.toUserEntity
import com.example.parkpal.domain.model.User
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Repository class responsible for handling user-related data operations.
 *
 * Acts as an abstraction layer over the UserDao, providing
 * domain models and mapping between database entities and domain objects.
 *
 * @property userDao The DAO interface for user database operations.
 */
@Singleton
class UserRepository @Inject constructor(private val userDao: UserDao) {

    /**
     * Inserts a new user into the database.
     *
     * @param user The User domain model to insert.
     * @return The row ID of the inserted user.
     */
    suspend fun insertUser(user: User): Long {
        Log.d("UserRepository", "Insert user: $user")
        return userDao.insertUser(user.toUserEntity())
    }

    /**
     * Updates an existing user in the database.
     *
     * @param user The User domain model with updated information.
     */
    suspend fun updateUser(user: User) {
        Log.d("UserRepository", "Update user: $user")
        userDao.updateUser(user.toUserEntity())
    }

    /**
     * Retrieves a user by their email address.
     *
     * @param email The email address to search for.
     * @return The User domain model if found, or null if not found.
     */
    suspend fun getUserByEmail(email: String): User? {
        Log.d("UserRepository", "Get user by email: $email")
        return userDao.getUserByEmail(email)?.toUser()
    }
}