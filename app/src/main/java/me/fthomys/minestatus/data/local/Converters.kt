package me.fthomys.minestatus.data.local

import androidx.room.TypeConverter
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import me.fthomys.minestatus.data.model.ServerStatus

/**
 * Type converters for Room database.
 */
class Converters {
    private val json = Json { ignoreUnknownKeys = true }

    @TypeConverter
    fun fromServerStatus(status: ServerStatus?): String? {
        return status?.let { json.encodeToString(it) }
    }

    @TypeConverter
    fun toServerStatus(statusString: String?): ServerStatus? {
        return statusString?.let { json.decodeFromString<ServerStatus>(it) }
    }
}