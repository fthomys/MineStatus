package me.fthomys.minestatus.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import me.fthomys.minestatus.data.model.Server

/**
 * The Room database for this app.
 */
@Database(entities = [Server::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class ServerDatabase : RoomDatabase() {

    abstract fun serverDao(): ServerDao

    companion object {
        @Volatile
        private var INSTANCE: ServerDatabase? = null

        fun getDatabase(context: Context): ServerDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ServerDatabase::class.java,
                    "server_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}