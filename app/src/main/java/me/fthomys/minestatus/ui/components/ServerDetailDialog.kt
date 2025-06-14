package me.fthomys.minestatus.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import me.fthomys.minestatus.data.model.Server
import me.fthomys.minestatus.data.model.ServerType
import me.fthomys.minestatus.ui.theme.MinecraftDarkBrown
import me.fthomys.minestatus.ui.theme.MinecraftGreen
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun ServerDetailDialog(
    server: Server,
    isLoading: Boolean,
    onDismiss: () -> Unit,
    onRefresh: (Server) -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            color = MinecraftDarkBrown
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = server.name,
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.weight(1f)
                    )
                    IconButton(onClick = onDismiss) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = Color.White
                        )
                    }
                }

                HorizontalDivider(
                    modifier = Modifier.padding(vertical = 8.dp),
                    thickness = DividerDefaults.Thickness, color = Color.White.copy(alpha = 0.3f)
                )
                InfoRow(label = "Address", value = "${server.address}:${server.port}")
                InfoRow(
                    label = "Type",
                    value = when (server.type) {
                        ServerType.JAVA -> "Java Edition"
                        ServerType.BEDROCK -> "Bedrock Edition"
                    }
                )
                server.lastStatus?.let { status ->
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "Status:",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(end = 8.dp)
                        )
                        Spacer(modifier = Modifier.size(8.dp))
                        androidx.compose.foundation.layout.Box(
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
                        Spacer(modifier = Modifier.height(8.dp))
                        status.version?.let {
                            InfoRow(label = "Version", value = it)
                        }
                        status.motd?.let {
                            InfoRow(label = "MOTD", value = it)
                        }
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
                            Text(
                                text = "Error: $it",
                                color = Color.Red,
                                style = MaterialTheme.typography.bodyMedium,
                                textAlign = TextAlign.Center,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp)
                            )
                        }
                    }
                    val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
                    val lastCheckedDate = Date(status.lastChecked)
                    InfoRow(
                        label = "Last checked",
                        value = dateFormat.format(lastCheckedDate)
                    )
                } ?: run {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "No status information available",
                        color = Color.White.copy(alpha = 0.7f),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = { onRefresh(server) },
                    enabled = !isLoading,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MinecraftGreen,
                        disabledContainerColor = MinecraftGreen.copy(alpha = 0.5f)
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(
                            color = Color.White,
                            modifier = Modifier.size(20.dp),
                            strokeWidth = 2.dp
                        )
                        Spacer(modifier = Modifier.size(8.dp))
                    } else {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "Refresh",
                            tint = Color.White
                        )
                        Spacer(modifier = Modifier.size(8.dp))
                    }
                    Text(
                        text = if (isLoading) "Refreshing..." else "Refresh Server Status",
                        color = Color.White
                    )
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
            color = Color.White,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(end = 8.dp)
        )
        Text(
            text = value,
            color = Color.White.copy(alpha = 0.7f)
        )
    }
}
