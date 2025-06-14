package me.fthomys.minestatus.data.network

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Data classes representing the response from the mcsrvstat.us API.
 * API documentation: https://api.mcsrvstat.us/
 */
@Serializable
data class McSrvStatResponse(
    val online: Boolean,
    val ip: String? = null,
    val port: Int? = null,
    val hostname: String? = null,
    val debug: DebugInfo? = null,
    val version: String? = null,
    val protocol: ProtocolInfo? = null,
    val icon: String? = null,
    val software: String? = null,
    val map: MapInfo? = null,
    val gamemode: String? = null,
    val serverid: String? = null,
    @SerialName("eula_blocked")
    val eulaBlocked: Boolean? = null,
    val motd: MotdInfo? = null,
    val players: PlayersInfo? = null,
    val plugins: List<PluginInfo>? = null,
    val mods: List<ModInfo>? = null,
    val info: InfoInfo? = null
)

@Serializable
data class DebugInfo(
    val ping: Boolean? = null,
    val query: Boolean? = null,
    val bedrock: Boolean? = null,
    val srv: Boolean? = null,
    val querymismatch: Boolean? = null,
    val ipinsrv: Boolean? = null,
    val cnameinsrv: Boolean? = null,
    val animatedmotd: Boolean? = null,
    val cachehit: Boolean? = null,
    val cachetime: Long? = null,
    val cacheexpire: Long? = null,
    val apiversion: Int? = null
)

@Serializable
data class ProtocolInfo(
    val version: Int? = null,
    val name: String? = null
)

@Serializable
data class MapInfo(
    val raw: String? = null,
    val clean: String? = null,
    val html: String? = null
)

@Serializable
data class MotdInfo(
    val raw: List<String>? = null,
    val clean: List<String>? = null,
    val html: List<String>? = null
)

@Serializable
data class PlayersInfo(
    val online: Int? = null,
    val max: Int? = null,
    val list: List<PlayerInfo>? = null
)

@Serializable
data class PlayerInfo(
    val name: String? = null,
    val uuid: String? = null
)

@Serializable
data class PluginInfo(
    val name: String? = null,
    val version: String? = null
)

@Serializable
data class ModInfo(
    val name: String? = null,
    val version: String? = null
)

@Serializable
data class InfoInfo(
    val raw: List<String>? = null,
    val clean: List<String>? = null,
    val html: List<String>? = null
)