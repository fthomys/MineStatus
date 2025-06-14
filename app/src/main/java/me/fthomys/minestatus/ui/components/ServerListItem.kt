package me.fthomys.minestatus.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import me.fthomys.minestatus.data.model.Server
import me.fthomys.minestatus.data.model.ServerType
import me.fthomys.minestatus.ui.theme.MinecraftDarkBrown
import me.fthomys.minestatus.ui.theme.MinecraftGreen
import me.fthomys.minestatus.ui.components.ServerIcon

@Composable
fun ServerListItem(
    server: Server,
    onServerClick: (Server) -> Unit,
    onFavoriteToggle: (Server) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onServerClick(server) },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MinecraftDarkBrown.copy(alpha = 0.8f)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier.size(40.dp),
                contentAlignment = Alignment.BottomEnd
            ) {
                ServerIcon(
                    base64Icon = server.lastStatus?.icon,
                    size = 40.dp
                )
                Box(
                    modifier = Modifier
                        .size(12.dp)
                        .background(
                            if (server.lastStatus?.online == true) MinecraftGreen else Color.Red,
                            shape = androidx.compose.foundation.shape.CircleShape
                        )
                        .border(1.dp, Color.Black, shape = androidx.compose.foundation.shape.CircleShape)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = server.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Row {
                    Text(
                        text = "${server.address}:${server.port}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.White.copy(alpha = 0.7f)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = when (server.type) {
                            ServerType.JAVA -> "Java"
                            ServerType.BEDROCK -> "Bedrock"
                        },
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.White.copy(alpha = 0.5f)
                    )
                }
                server.lastStatus?.let { status ->
                    if (status.online) {
                        Text(
                            text = "Players: ${status.currentPlayers ?: 0}/${status.maxPlayers ?: 0}",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.White.copy(alpha = 0.7f)
                        )
                        status.version?.let {
                            Text(
                                text = "Version: $it",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.White.copy(alpha = 0.7f)
                            )
                        }
                    } else {
                        Text(
                            text = "Status: Offline" + (status.error?.let { " - ${it.take(30)}${if (it.length > 30) "..." else ""}" } ?: ""),
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.Red.copy(alpha = 0.8f)
                        )
                    }
                }
            }
            IconButton(onClick = { onFavoriteToggle(server) }) {
                Icon(
                    imageVector = if (server.favorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                    contentDescription = if (server.favorite) "Remove from favorites" else "Add to favorites",
                    tint = if (server.favorite) Color.Yellow else Color.White
                )
            }
        }
    }
}
