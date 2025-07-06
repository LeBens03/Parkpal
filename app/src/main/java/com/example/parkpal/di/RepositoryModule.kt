package com.example.parkpal.di

import android.content.Context
import com.example.parkpal.data.dao.CarDao
import com.example.parkpal.data.dao.ParkingHistoryDao
import com.example.parkpal.data.dao.ParkingLocationDao
import com.example.parkpal.data.dao.UserDao
import com.example.parkpal.domain.repository.CarRepository
import com.example.parkpal.domain.repository.ParkingHistoryRepository
import com.example.parkpal.domain.repository.ParkingLocationRepository
import com.example.parkpal.domain.repository.UserRepository
import com.example.parkpal.presentation.utils.UserLocationImpl
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Provides repository instances and location-related dependencies for DI.
 */
@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    /**
     * Provides a singleton UserRepository instance.
     */
    @Singleton
    @Provides
    fun provideUserRepository(userDao: UserDao): UserRepository =
        UserRepository(userDao)

    /**
     * Provides a singleton CarRepository instance.
     */
    @Singleton
    @Provides
    fun provideCarRepository(carDao: CarDao): CarRepository =
        CarRepository(carDao)

    /**
     * Provides a singleton ParkingLocationRepository instance.
     */
    @Singleton
    @Provides
    fun provideParkingLocationRepository(parkingLocationDao: ParkingLocationDao): ParkingLocationRepository =
        ParkingLocationRepository(parkingLocationDao)

    /**
     * Provides a singleton FusedLocationProviderClient instance for location services.
     */
    @Singleton
    @Provides
    fun provideFusedLocationProviderClient(
        @ApplicationContext context: Context
    ): FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context)

    /**
     * Provides a singleton UserLocationImpl instance, implementing UserLocation interface.
     */
    @Singleton
    @Provides
    fun provideUserLocationImpl(
        @ApplicationContext context: Context,
        fusedLocationClient: FusedLocationProviderClient
    ): UserLocationImpl =
        UserLocationImpl(context, fusedLocationClient)

    /**
     * Provides a singleton ParkingHistoryRepository instance.
     */
    @Singleton
    @Provides
    fun provideParkingHistoryRepository(parkingHistoryDao: ParkingHistoryDao): ParkingHistoryRepository =
        ParkingHistoryRepository(parkingHistoryDao)
}