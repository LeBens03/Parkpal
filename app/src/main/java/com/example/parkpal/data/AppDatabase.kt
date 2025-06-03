package com.example.parkpal.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

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