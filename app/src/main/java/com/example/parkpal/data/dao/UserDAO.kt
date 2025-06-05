package com.example.parkpal.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.parkpal.data.model.ParkingHistory
import com.example.parkpal.data.model.PublicParking
import com.example.parkpal.data.model.User
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDAO {
    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun insertUser(user: User): Long

    @Update
    suspend fun updateUser(user: User)

    @Delete
    suspend fun deleteUser(user: User)

    @Query("SELECT * FROM user_table")
    fun getAllUsers(): Flow<List<User>>

    @Query("SELECT * FROM user_table WHERE userId = :userId")
    suspend fun getUserById(userId: Int): User

    @Transaction
    @Query("""
        SELECT * FROM parking_history_table 
        JOIN car_table ON parking_history_table.carId = car_table.carId
        WHERE userId = :userId
    """)
    fun getCarsParkingHistory(userId: Int): Flow<List<ParkingHistory>>

    @Transaction
    @Query("""
        SELECT * FROM public_parking_table
        JOIN user_public_parking_table ON public_parking_table.publicParkingId = user_public_parking_table.publicParkingId
        WHERE userId = :userId
    """)
    fun getSavedParkings(userId: Int): Flow<List<PublicParking>>?
}