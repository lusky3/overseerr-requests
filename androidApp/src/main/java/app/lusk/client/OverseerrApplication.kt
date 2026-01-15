package app.lusk.client

import android.app.Application
import app.lusk.client.di.initKoin
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.workmanager.koin.workManagerFactory

/**
 * Application class for Overseerr Android Client.
 */
class OverseerrApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        
        initKoin(this) {
            androidLogger()
            androidContext(this@OverseerrApplication)
            workManagerFactory()
            modules(app.lusk.client.di.androidAppModule)
        }
        
    }
}
