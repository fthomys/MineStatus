package me.fthomys.minestatus.ui.components

import android.graphics.BitmapFactory
import android.util.Base64
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import me.fthomys.minestatus.R
import me.fthomys.minestatus.ui.theme.MinecraftDarkBrown

/**
 * A composable that displays a Minecraft server icon.
 * 
 * @param base64Icon The Base64-encoded server icon
 * @param size The size of the icon
 * @param modifier Additional modifier for the icon
 */
@Composable
fun ServerIcon(
    base64Icon: String?,
    size: Dp = 64.dp,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var bitmap by remember { mutableStateOf<android.graphics.Bitmap?>(null) }
    var isLoading by remember { mutableStateOf(false) }
    if (!base64Icon.isNullOrEmpty() && bitmap == null && !isLoading) {
        isLoading = true
        CoroutineScope(Dispatchers.Main).launch {
            try {
                val iconData = withContext(Dispatchers.IO) {
                    val cleanBase64 = if (base64Icon.contains(",")) {
                        base64Icon.split(",")[1]
                    } else {
                        base64Icon
                    }
                    
                    Base64.decode(cleanBase64, Base64.DEFAULT)
                }
                
                bitmap = BitmapFactory.decodeByteArray(iconData, 0, iconData.size)
            } catch (e: Exception) {
            } finally {
                isLoading = false
            }
        }
    }
    
    Box(
        modifier = modifier
            .size(size)
            .clip(RoundedCornerShape(4.dp))
            .background(MinecraftDarkBrown)
            .border(1.dp, Color.Black, RoundedCornerShape(4.dp)),
        contentAlignment = Alignment.Center
    ) {
        when {
            isLoading -> {
                CircularProgressIndicator(
                    modifier = Modifier.size(size / 2),
                    color = Color.White
                )
            }
            bitmap != null -> {
                Image(
                    bitmap = bitmap!!.asImageBitmap(),
                    contentDescription = "Server Icon",
                    modifier = Modifier.size(size),
                    contentScale = ContentScale.Crop
                )
            }
            else -> {
                Image(
                    painter = painterResource(id = R.drawable.ic_launcher_foreground),
                    contentDescription = "Default Server Icon",
                    modifier = Modifier.size(size),
                    contentScale = ContentScale.Crop
                )
            }
        }
    }
}