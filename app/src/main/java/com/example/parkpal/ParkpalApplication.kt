package com.example.parkpal

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * Application class for ParkPal app.
 *
 * Annotated with [HiltAndroidApp] to trigger Hilt's code generation
 * and enable dependency injection throughout the app.
 */
@HiltAndroidApp
class ParkpalApplication : Application()