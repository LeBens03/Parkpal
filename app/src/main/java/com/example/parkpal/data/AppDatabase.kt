package com.example.parkpal.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.parkpal.data.dao.CarDao
import com.example.parkpal.data.dao.ParkingHistoryDao
import com.example.parkpal.data.dao.ParkingLocationDao
import com.example.parkpal.data.dao.UserDao
import com.example.parkpal.data.entity.CarEntity
import com.example.parkpal.data.entity.ParkingHistoryEntity
import com.example.parkpal.data.entity.ParkingLocationEntity
import com.example.parkpal.data.entity.UserEntity

/**
 * The main Room database class for the ParkPal app.
 *
 * This class serves as the database holder and the main access point for the
 * underlying SQLite database.
 *
 * @Database annotation specifies:
 * - The list of entities (tables) included in the database.
 * - The database version number, used for migrations.
 * - exportSchema is set to false to avoid exporting the schema files.
 *
 * Provides abstract methods to get DAO instances for accessing the tables.
 */
@Database(
    entities = [
        UserEntity::class,
        CarEntity::class,
        ParkingLocationEntity::class,
        ParkingHistoryEntity::class
    ],
    version = 13,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    /**
     * Provides access to User-related database operations.
     */
    abstract fun userDao(): UserDao

    /**
     * Provides access to Car-related database operations.
     */
    abstract fun carDao(): CarDao

    /**
     * Provides access to ParkingLocation-related database operations.
     */
    abstract fun parkingLocationDao(): ParkingLocationDao

    /**
     * Provides access to ParkingHistory-related database operations.
     */
    abstract fun parkingHistoryDao(): ParkingHistoryDao
}