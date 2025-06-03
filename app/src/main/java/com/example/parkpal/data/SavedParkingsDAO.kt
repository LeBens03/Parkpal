package com.example.parkpal.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface SavedParkingsDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addSavedPublicParking(savedPublicParking: PublicParking): Long

    @Query("DELETE FROM user_public_parking_table WHERE userId = :userId AND publicParkingId = :publicParkingId")
    suspend fun removeSavedPublicParking(userId: Int, publicParkingId: Int)
}