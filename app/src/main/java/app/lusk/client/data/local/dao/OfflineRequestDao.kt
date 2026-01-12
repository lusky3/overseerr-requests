package app.lusk.client.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import app.lusk.client.data.local.entity.OfflineRequestEntity

@Dao
interface OfflineRequestDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(request: OfflineRequestEntity): Long

    @Query("SELECT * FROM offline_requests")
    suspend fun getAll(): List<OfflineRequestEntity>

    @Delete
    suspend fun delete(request: OfflineRequestEntity)
    
    @Query("DELETE FROM offline_requests WHERE id = :id")
    suspend fun deleteById(id: Int)
}
