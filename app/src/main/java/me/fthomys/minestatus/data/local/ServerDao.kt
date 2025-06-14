package me.fthomys.minestatus.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import me.fthomys.minestatus.data.model.Server

/**
 * Data Access Object for the Server table.
 */
@Dao
interface ServerDao {
    
    @Query("SELECT * FROM servers ORDER BY name ASC")
    fun getAllServers(): Flow<List<Server>>
    
    @Query("SELECT * FROM servers WHERE favorite = 1 ORDER BY name ASC")
    fun getFavoriteServers(): Flow<List<Server>>
    
    @Query("SELECT * FROM servers WHERE id = :id")
    suspend fun getServerById(id: Long): Server?
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertServer(server: Server): Long
    
    @Update
    suspend fun updateServer(server: Server)
    
    @Delete
    suspend fun deleteServer(server: Server)
    
    @Query("DELETE FROM servers WHERE id = :id")
    suspend fun deleteServerById(id: Long)
}