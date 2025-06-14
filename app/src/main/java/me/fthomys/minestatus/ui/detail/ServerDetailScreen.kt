package me.fthomys.minestatus.ui.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import me.fthomys.minestatus.data.model.Server
import me.fthomys.minestatus.data.model.ServerType
import me.fthomys.minestatus.ui.components.MinecraftHtmlText
import me.fthomys.minestatus.ui.components.MinecraftMultilineText
import me.fthomys.minestatus.ui.components.MinecraftText
import me.fthomys.minestatus.ui.components.ServerIcon
import me.fthomys.minestatus.ui.main.MainViewModel
import me.fthomys.minestatus.ui.theme.MinecraftDarkBrown
import me.fthomys.minestatus.ui.theme.MinecraftDarkGray
import me.fthomys.minestatus.ui.theme.MinecraftGreen
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ServerDetailScreen(
    serverId: Long,
    viewModel: MainViewModel,
    onNavigateBack: () -> Unit,
    onEditServer: (Server) -> Unit,
    onDeleteServer: (Server) -> Unit = { viewModel.deleteServer(it); onNavigateBack() }
) {
    val servers by viewModel.servers.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val server = servers.find { it.id == serverId }

    if (server == null) {
        Text(
            text = "Server not found",
            modifier = Modifier.padding(16.dp)
        )
        return
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = server.name,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MinecraftDarkBrown
                )
            )
        },
        floatingActionButton = {
            Row(
                modifier = Modifier.padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                FloatingActionButton(
                    onClick = { onDeleteServer(server) },
                    containerColor = Color.Red
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete server",
                        tint = Color.White
                    )
                }
                FloatingActionButton(
                    onClick = { onEditServer(server) },
                    containerColor = MinecraftGreen
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Edit server",
                        tint = Color.White
                    )
                }
                FloatingActionButton(
                    onClick = { viewModel.refreshServerStatus(server) },
                    containerColor = MinecraftGreen
                ) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = "Refresh server status",
                        tint = Color.White
                    )
                }
            }
        },
        containerColor = MinecraftDarkGray
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                shape = RoundedCornerShape(8.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        server.lastStatus?.let { status ->
                            ServerIcon(
                                base64Icon = status.icon,
                                size = 64.dp,
                                modifier = Modifier.padding(end = 16.dp)
                            )
                        } ?: ServerIcon(
                            base64Icon = null,
                            size = 64.dp,
                            modifier = Modifier.padding(end = 16.dp)
                        )
                        Column {
                            Text(
                                text = "Server Information",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )

                            Text(
                                text = "${server.address}:${server.port}",
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color.Gray
                            )

                            Text(
                                text = when (server.type) {
                                    ServerType.JAVA -> "Java Edition"
                                    ServerType.BEDROCK -> "Bedrock Edition"
                                },
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.Gray
                            )
                        }
                    }

                    Divider(modifier = Modifier.padding(vertical = 16.dp))
                    InfoRow(label = "Address", value = "${server.address}:${server.port}")
                    InfoRow(
                        label = "Type",
                        value = when (server.type) {
                            ServerType.JAVA -> "Java Edition"
                            ServerType.BEDROCK -> "Bedrock Edition"
                        }
                    )
                }
            }
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                shape = RoundedCornerShape(8.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(
                        text = "Server Status",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    Divider(modifier = Modifier.padding(vertical = 8.dp))

                    server.lastStatus?.let { status ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "Status:",
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(end = 8.dp)
                            )
                            Spacer(modifier = Modifier.size(8.dp))
                            Box(
                                modifier = Modifier
                                    .size(12.dp)
                                    .background(
                                        if (status.online) MinecraftGreen else Color.Red,
                                        CircleShape
                                    )
                            )
                            Spacer(modifier = Modifier.size(8.dp))
                            Text(
                                text = if (status.online) "Online" else "Offline",
                                color = if (status.online) MinecraftGreen else Color.Red
                            )
                        }

                        if (status.online) {
                            Spacer(modifier = Modifier.height(16.dp))
                            status.version?.let {
                                InfoRow(label = "Version", value = it)
                            }
                            if (status.motdHtml != null || status.motdRaw != null || status.motdClean != null || status.motd != null) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 8.dp)
                                ) {
                                    Text(
                                        text = "MOTD:",
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(bottom = 4.dp)
                                    )
                                    Card(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(top = 4.dp),
                                        colors = CardDefaults.cardColors(
                                            containerColor = Color.Black.copy(alpha = 0.7f)
                                        ),
                                        shape = RoundedCornerShape(4.dp)
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(8.dp)
                                        ) {
                                            when {
                                                status.motdHtml != null && status.motdHtml.isNotEmpty() -> {
                                                    Column {
                                                        status.motdHtml.forEach { line ->
                                                            MinecraftHtmlText(
                                                                html = line,
                                                                modifier = Modifier.fillMaxWidth()
                                                            )
                                                        }
                                                    }
                                                }
                                                status.motdRaw != null && status.motdRaw.isNotEmpty() -> {
                                                    MinecraftMultilineText(
                                                        lines = status.motdRaw,
                                                        modifier = Modifier.fillMaxWidth()
                                                    )
                                                }
                                                status.motdClean != null && status.motdClean.isNotEmpty() -> {
                                                    Column {
                                                        status.motdClean.forEach { line ->
                                                            Text(
                                                                text = line,
                                                                color = Color.White,
                                                                modifier = Modifier.fillMaxWidth()
                                                            )
                                                        }
                                                    }
                                                }
                                                else -> {
                                                    Text(
                                                        text = status.motd ?: "No MOTD available",
                                                        color = Color.White,
                                                        modifier = Modifier.fillMaxWidth()
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(8.dp))
                            InfoRow(
                                label = "Players",
                                value = "${status.currentPlayers ?: 0}/${status.maxPlayers ?: 0}"
                            )
                            status.ping?.let {
                                InfoRow(label = "Ping", value = "$it ms")
                            }
                        } else {
                            Spacer(modifier = Modifier.height(8.dp))
                            status.error?.let {
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 8.dp),
                                    colors = CardDefaults.cardColors(
                                        containerColor = Color.Red.copy(alpha = 0.1f)
                                    ),
                                    shape = RoundedCornerShape(4.dp)
                                ) {
                                    Text(
                                        text = "Error: $it",
                                        color = Color.Red,
                                        style = MaterialTheme.typography.bodyMedium,
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(16.dp)
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))
                        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
                        val lastCheckedDate = Date(status.lastChecked)
                        InfoRow(
                            label = "Last checked",
                            value = dateFormat.format(lastCheckedDate)
                        )
                    } ?: run {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = Color.Gray.copy(alpha = 0.1f)
                            ),
                            shape = RoundedCornerShape(4.dp)
                        ) {
                            Text(
                                text = "No status information available",
                                color = Color.Gray,
                                textAlign = TextAlign.Center,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp)
                            )
                        }
                    }

                }
            }
        }
    }
}

@Composable
private fun InfoRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "$label:",
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(end = 8.dp)
        )
        Text(
            text = value
        )
    }
}
