package me.fthomys.minestatus.ui.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import me.fthomys.minestatus.data.local.ServerDatabase
import me.fthomys.minestatus.data.model.Server
import me.fthomys.minestatus.data.model.ServerStatus
import me.fthomys.minestatus.data.model.ServerType
import me.fthomys.minestatus.data.network.ServerStatusChecker
import me.fthomys.minestatus.data.repository.ServerRepository

/**
 * ViewModel for the main screen.
 */
class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: ServerRepository
    private val serverStatusChecker = ServerStatusChecker()
    private val _servers = MutableStateFlow<List<Server>>(emptyList())
    val servers: StateFlow<List<Server>> = _servers.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()


    init {
        val serverDao = ServerDatabase.getDatabase(application).serverDao()
        repository = ServerRepository(serverDao)

        viewModelScope.launch {
            repository.allServers.collectLatest { serverList ->
                _servers.value = serverList
            }
        }
    }

    fun addServer(name: String, address: String, port: Int = 25565, type: ServerType = ServerType.JAVA) {
        viewModelScope.launch {
            val newServer = Server(
                name = name,
                address = address,
                port = port,
                type = type
            )
            repository.addServer(newServer)
        }
    }

    fun updateServer(server: Server) {
        viewModelScope.launch {
            repository.updateServer(server)
        }
    }

    fun deleteServer(server: Server) {
        viewModelScope.launch {
            repository.deleteServer(server)
        }
    }

    fun toggleFavorite(server: Server) {
        viewModelScope.launch {
            repository.toggleFavorite(server.id)
        }
    }

    fun refreshServerStatus(server: Server) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val status = serverStatusChecker.checkServerStatus(
                    address = server.address,
                    port = server.port,
                    type = server.type
                )
                repository.updateServerStatus(server.id, status)
            } catch (e: Exception) {
                val status = ServerStatus(online = false)
                repository.updateServerStatus(server.id, status)
            } finally {
                _isLoading.value = false
            }
        }
    }


    fun refreshAllServers() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                _servers.value.forEach { server ->
                    refreshServerStatus(server)
                }
            } finally {
                _isLoading.value = false
            }
        }
    }
}
