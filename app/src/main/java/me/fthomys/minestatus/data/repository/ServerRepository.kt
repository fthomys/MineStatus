package me.fthomys.minestatus.data.repository

import kotlinx.coroutines.flow.Flow
import me.fthomys.minestatus.data.local.ServerDao
import me.fthomys.minestatus.data.model.Server
import me.fthomys.minestatus.data.model.ServerStatus

/**
 * Repository for managing server data.
 */
class ServerRepository(private val serverDao: ServerDao) {

    val allServers: Flow<List<Server>> = serverDao.getAllServers()
    val favoriteServers: Flow<List<Server>> = serverDao.getFavoriteServers()

    suspend fun getServer(id: Long): Server? {
        return serverDao.getServerById(id)
    }

    suspend fun addServer(server: Server): Long {
        return serverDao.insertServer(server)
    }

    suspend fun updateServer(server: Server) {
        serverDao.updateServer(server)
    }

    suspend fun deleteServer(server: Server) {
        serverDao.deleteServer(server)
    }

    suspend fun deleteServer(id: Long) {
        serverDao.deleteServerById(id)
    }

    suspend fun updateServerStatus(id: Long, status: ServerStatus) {
        val server = serverDao.getServerById(id)
        server?.let {
            serverDao.updateServer(it.copy(lastStatus = status))
        }
    }

    suspend fun toggleFavorite(id: Long) {
        val server = serverDao.getServerById(id)
        server?.let {
            serverDao.updateServer(it.copy(favorite = !it.favorite))
        }
    }
}