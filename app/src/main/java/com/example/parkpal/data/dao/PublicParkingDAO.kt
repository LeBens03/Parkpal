package com.example.parkpal.data.dao

import androidx.room.Dao
import androidx.room.Query
import com.example.parkpal.data.model.PublicParking

@Dao
interface PublicParkingDAO {
    @Query("SELECT * FROM public_parking_table")
    suspend fun getAllPublicParkings(): List<PublicParking>
}