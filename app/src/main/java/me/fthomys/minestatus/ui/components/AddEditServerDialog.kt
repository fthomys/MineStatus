package me.fthomys.minestatus.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import me.fthomys.minestatus.data.model.Server
import me.fthomys.minestatus.data.model.ServerType
import me.fthomys.minestatus.ui.theme.MinecraftDarkBrown
import me.fthomys.minestatus.ui.theme.MinecraftGreen

@Composable
fun AddEditServerDialog(
    server: Server? = null,
    onDismiss: () -> Unit,
    onConfirm: (String, String, Int, ServerType) -> Unit
) {
    var serverName by remember { mutableStateOf(server?.name ?: "") }
    var serverAddress by remember { mutableStateOf(server?.address ?: "") }
    var serverPort by remember { mutableStateOf(server?.port?.toString() ?: "25565") }
    var serverType by remember { mutableStateOf(server?.type ?: ServerType.JAVA) }
    var nameError by remember { mutableStateOf(false) }
    var addressError by remember { mutableStateOf(false) }
    var portError by remember { mutableStateOf(false) }

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
                Text(
                    text = if (server == null) "Add Server" else "Edit Server",
                    color = Color.White,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                OutlinedTextField(
                    value = serverName,
                    onValueChange = { 
                        serverName = it
                        nameError = it.isBlank()
                    },
                    label = { Text("Server Name") },
                    isError = nameError,
                    supportingText = { if (nameError) Text("Name cannot be empty") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.White.copy(alpha = 0.1f),
                        unfocusedContainerColor = Color.White.copy(alpha = 0.1f),
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White
                    )
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = serverAddress,
                    onValueChange = { 
                        serverAddress = it
                        addressError = it.isBlank()
                    },
                    label = { Text("Server Address") },
                    isError = addressError,
                    supportingText = { if (addressError) Text("Address cannot be empty") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.White.copy(alpha = 0.1f),
                        unfocusedContainerColor = Color.White.copy(alpha = 0.1f),
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White
                    )
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = serverPort,
                    onValueChange = { 
                        serverPort = it
                        portError = it.toIntOrNull() == null || it.toInt() <= 0
                    },
                    label = { Text("Server Port") },
                    isError = portError,
                    supportingText = { if (portError) Text("Port must be a positive number") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.White.copy(alpha = 0.1f),
                        unfocusedContainerColor = Color.White.copy(alpha = 0.1f),
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Server Type",
                    color = Color.White,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .selectable(
                            selected = serverType == ServerType.JAVA,
                            onClick = { serverType = ServerType.JAVA }
                        )
                        .padding(vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = serverType == ServerType.JAVA,
                        onClick = { serverType = ServerType.JAVA }
                    )
                    Text(
                        text = "Java Edition (Default port: 25565)",
                        color = Color.White,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .selectable(
                            selected = serverType == ServerType.BEDROCK,
                            onClick = { 
                                serverType = ServerType.BEDROCK
                                if (serverPort == "25565") {
                                    serverPort = "19132"
                                }
                            }
                        )
                        .padding(vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = serverType == ServerType.BEDROCK,
                        onClick = { 
                            serverType = ServerType.BEDROCK
                            if (serverPort == "25565") {
                                serverPort = "19132"
                            }
                        }
                    )
                    Text(
                        text = "Bedrock Edition (Default port: 19132)",
                        color = Color.White,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        nameError = serverName.isBlank()
                        addressError = serverAddress.isBlank()
                        portError = serverPort.toIntOrNull() == null || serverPort.toInt() <= 0

                        if (!nameError && !addressError && !portError) {
                            onConfirm(serverName, serverAddress, serverPort.toInt(), serverType)
                            onDismiss()
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MinecraftGreen
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "Save")
                }

                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = onDismiss,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Gray
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "Cancel")
                }
            }
        }
    }
}
