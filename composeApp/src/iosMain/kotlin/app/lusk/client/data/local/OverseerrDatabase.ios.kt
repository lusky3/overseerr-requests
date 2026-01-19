package app.lusk.client.data.local

import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import app.lusk.client.util.PlatformContext
import platform.Foundation.NSHomeDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSUserDomainMask

actual fun getDatabaseBuilder(context: PlatformContext): RoomDatabase.Builder<OverseerrDatabase> {
    val dbFilePath = NSHomeDirectory() + "/Documents/" + OverseerrDatabase.DATABASE_NAME
    return Room.databaseBuilder<OverseerrDatabase>(
        name = dbFilePath
    )
    .setDriver(BundledSQLiteDriver())
}

