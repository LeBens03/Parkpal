package com.example.parkpal.data

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction

@Dao
interface PublicParkingDAO {
    @Query("SELECT * FROM public_parking_table")
    suspend fun getAllPublicParkings(): List<PublicParking>

    @Transaction
    @Query("SELECT * FROM public_parking_table WHERE id IN (SELECT publicParkingId FROM user_public_parking_table WHERE userId = :userId)")
    suspend fun getUserSavedPublicParkings(userId: Int): List<PublicParking>
}
