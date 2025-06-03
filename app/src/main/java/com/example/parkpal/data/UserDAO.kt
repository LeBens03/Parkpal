package com.example.parkpal.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update

@Dao
interface UserDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: User): Long

    @Update
    suspend fun updateUser(user: User)

    @Delete
    suspend fun deleteUser(user: User)

    @Query("SELECT * FROM user_table WHERE userId = :userId")
    suspend fun getUserById(userId: Int): User

    @Transaction
    @Query("SELECT * FROM user_table WHERE userId = :userId")
    suspend fun getUserWithCarWithParkingHistory(userId: Int): UserWithCarWithParkingHistory?

    @Transaction
    @Query("SELECT * FROM user_table WHERE userId = :userId")
    suspend fun getUserWithPublicParking(userId: Int): UserWithPublicParking?
}