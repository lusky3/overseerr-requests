package com.example.overseerr_client

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * Application class for Overseerr Android Client.
 * Annotated with @HiltAndroidApp to enable Hilt dependency injection.
 */
@HiltAndroidApp
class OverseerrApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        // Application initialization will be added here
    }
}
