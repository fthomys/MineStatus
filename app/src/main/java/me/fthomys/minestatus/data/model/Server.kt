package me.fthomys.minestatus.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

/**
 * Enum representing the type of Minecraft server.
 */
@Serializable
enum class ServerType {
    JAVA,
    BEDROCK
}

/**
 * Data class representing a Minecraft server.
 */
@Entity(tableName = "servers")
@Serializable
data class Server(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val address: String,
    val port: Int = 25565,
    val type: ServerType = ServerType.JAVA,
    val favorite: Boolean = false,
    val lastStatus: ServerStatus? = null
)

/**
 * Data class representing the status of a Minecraft server.
 */
@Serializable
data class ServerStatus(
    val online: Boolean,
    val version: String? = null,
    val motd: String? = null,
    val motdRaw: List<String>? = null,
    val motdClean: List<String>? = null,
    val motdHtml: List<String>? = null,
    val icon: String? = null,
    val currentPlayers: Int? = null,
    val maxPlayers: Int? = null,
    val ping: Long? = null,
    val error: String? = null,
    val lastChecked: Long = System.currentTimeMillis()
)
