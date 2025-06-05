package com.example.parkpal.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.parkpal.data.dao.CarDAO
import com.example.parkpal.data.dao.ParkingHistoryDAO
import com.example.parkpal.data.dao.ParkingLocationDAO
import com.example.parkpal.data.dao.PublicParkingDAO
import com.example.parkpal.data.dao.SavedParkingsDAO
import com.example.parkpal.data.dao.UserDAO
import com.example.parkpal.data.model.Car
import com.example.parkpal.data.model.ParkingHistory
import com.example.parkpal.data.model.ParkingLocation
import com.example.parkpal.data.model.PublicParking
import com.example.parkpal.data.model.User
import com.example.parkpal.data.model.UserPublicParking

@Database(
    entities = [
        User::class,
        PublicParking::class,
        UserPublicParking::class,
        Car::class,
        ParkingLocation::class,
        ParkingHistory::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun carDao(): CarDAO
    abstract fun parkingHistoryDao(): ParkingHistoryDAO
    abstract fun parkingLocationDao(): ParkingLocationDAO
    abstract fun publicParkingDao(): PublicParkingDAO
    abstract fun savedParkingsDao(): SavedParkingsDAO
    abstract fun userDao(): UserDAO

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "parkpal_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}