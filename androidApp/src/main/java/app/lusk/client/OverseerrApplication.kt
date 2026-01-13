package app.lusk.client

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
        
        // Start mock server in debug mode for testing
        if (app.lusk.client.BuildConfig.DEBUG) {
            startMockServer()
        }
    }
    
    private fun startMockServer() {
        Thread {
            try {
                app.lusk.client.mock.MockOverseerrServer().apply {
                    start(5055)
                }
                android.util.Log.d("OverseerrApp", "Mock server started on port 5055")
            } catch (e: Exception) {
                android.util.Log.e("OverseerrApp", "Failed to start mock server", e)
            }
        }.start()
    }
}
