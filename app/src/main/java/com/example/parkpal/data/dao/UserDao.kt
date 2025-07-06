package com.example.parkpal.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.parkpal.data.entity.UserEntity
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object (DAO) interface for performing database operations on UserEntity.
 * Uses Room annotations to define SQL queries and operations.
 */
@Dao
interface UserDao {

    /**
     * Inserts a new user into the database.
     * If a user with the same primary key already exists, it replaces it.
     *
     * @param user The UserEntity object to insert.
     * @return The row ID of the inserted user.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UserEntity): Long

    /**
     * Updates an existing user record in the database.
     * Matches the user by primary key.
     *
     * @param user The UserEntity object with updated data.
     */
    @Update
    suspend fun updateUser(user: UserEntity)

    /**
     * Deletes a user record from the database.
     *
     * @param user The UserEntity object to delete.
     */
    @Delete
    suspend fun deleteUser(user: UserEntity)

    /**
     * Retrieves a Flow stream of all users from the database.
     * Using Flow allows observing the data and receiving updates automatically.
     *
     * @return Flow emitting a list of all UserEntity objects.
     */
    @Query("SELECT * FROM user_table")
    fun getAllUsers(): Flow<List<UserEntity>>

    /**
     * Retrieves a user by their email address.
     *
     * @param email The email of the user to find.
     * @return The UserEntity object matching the email, or null if no user found.
     */
    @Query("SELECT * FROM user_table WHERE email = :email")
    suspend fun getUserByEmail(email: String): UserEntity?
}