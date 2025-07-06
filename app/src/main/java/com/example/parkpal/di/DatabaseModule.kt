package com.example.parkpal.di

import android.content.Context
import androidx.room.Room
import com.example.parkpal.data.AppDatabase
import com.example.parkpal.data.dao.CarDao
import com.example.parkpal.data.dao.ParkingLocationDao
import com.example.parkpal.data.dao.UserDao
import com.example.parkpal.data.dao.ParkingHistoryDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Provides dependencies related to the Room database and DAOs for dependency injection.
 */
@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    /**
     * Provides a singleton instance of the Room database [AppDatabase].
     *
     * @param context Application context injected by Hilt.
     * @return Singleton [AppDatabase] instance.
     */
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "parkpal_database"
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    /**
     * Provides the [UserDao] from the database.
     */
    @Provides
    fun provideUserDao(database: AppDatabase): UserDao = database.userDao()

    /**
     * Provides the [CarDao] from the database.
     */
    @Provides
    fun provideCarDao(database: AppDatabase): CarDao = database.carDao()

    /**
     * Provides the [ParkingLocationDao] from the database.
     */
    @Provides
    fun provideParkingLocationDao(database: AppDatabase): ParkingLocationDao = database.parkingLocationDao()

    /**
     * Provides the [ParkingHistoryDao] from the database.
     */
    @Provides
    fun provideParkingHistoryDao(database: AppDatabase): ParkingHistoryDao = database.parkingHistoryDao()
}