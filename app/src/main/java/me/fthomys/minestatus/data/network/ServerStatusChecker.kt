package me.fthomys.minestatus.data.network

import android.util.Log
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import me.fthomys.minestatus.data.model.ServerStatus
import me.fthomys.minestatus.data.model.ServerType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import java.io.IOException
import java.net.UnknownHostException
import java.util.concurrent.TimeUnit

/**
 * A class for checking the status of Minecraft servers using the mcsrvstat.us API.
 * Supports both Java and Bedrock editions.
 */
class ServerStatusChecker {

    companion object {
        private const val TAG = "MineStatusChecker"
        private const val MAX_RETRIES = 3
        private const val RETRY_DELAY_MS = 1000L
    }

    private val json = Json { ignoreUnknownKeys = true }

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .connectTimeout(10, TimeUnit.SECONDS)
        .readTimeout(10, TimeUnit.SECONDS)
        .writeTimeout(10, TimeUnit.SECONDS)
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(McSrvStatApi.BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .build()

    private val api = retrofit.create(McSrvStatApi::class.java)

    /**
     * Check the status of a Minecraft server using the mcsrvstat.us API.
     * 
     * @param address The server address
     * @param port The server port
     * @param type The server type (Java or Bedrock)
     * @return The server status
     */
    suspend fun checkServerStatus(address: String, port: Int, type: ServerType): ServerStatus = withContext(Dispatchers.IO) {
        Log.d(TAG, "Checking server status: $address:$port (${type.name})")

        var lastException: Exception? = null
        val fullAddress = "$address:$port"

        for (attempt in 1..MAX_RETRIES) {
            try {
                Log.d(TAG, "Attempt $attempt of $MAX_RETRIES")

                val response = when (type) {
                    ServerType.JAVA -> api.getJavaServerStatus(fullAddress)
                    ServerType.BEDROCK -> api.getBedrockServerStatus(fullAddress)
                }

                Log.d(TAG, "API response: $response")

                val serverStatus = ServerStatus(
                    online = response.online,
                    version = response.version,
                    motd = response.motd?.clean?.firstOrNull() ?: response.motd?.raw?.firstOrNull(),
                    motdRaw = response.motd?.raw,
                    motdClean = response.motd?.clean,
                    motdHtml = response.motd?.html,
                    icon = response.icon,
                    currentPlayers = response.players?.online,
                    maxPlayers = response.players?.max,
                    ping = null, // API doesn't provide ping time
                    error = if (!response.online) "Server is offline" else null,
                    lastChecked = System.currentTimeMillis()
                )

                if (serverStatus.online) {
                    Log.d(TAG, "Server is online: $address:$port")
                } else {
                    Log.d(TAG, "Server appears to be offline: $address:$port")
                }

                return@withContext serverStatus
            } catch (e: UnknownHostException) {
                lastException = e
                Log.e(TAG, "Unknown host: $address", e)
                break
            } catch (e: IOException) {
                lastException = e
                Log.e(TAG, "Network error checking server status (attempt $attempt): ${e.message}", e)
                if (attempt < MAX_RETRIES) {
                    val delayTime = RETRY_DELAY_MS * attempt
                    Log.d(TAG, "Retrying in $delayTime ms...")
                    delay(delayTime)
                }
            } catch (e: Exception) {
                lastException = e
                Log.e(TAG, "Error checking server status (attempt $attempt): ${e.message}", e)
                if (attempt < MAX_RETRIES) {
                    val delayTime = RETRY_DELAY_MS * attempt
                    Log.d(TAG, "Retrying in $delayTime ms...")
                    delay(delayTime)
                }
            }
        }
        Log.e(TAG, "All attempts to check server status failed: $address:$port", lastException)
        return@withContext ServerStatus(
            online = false,
            error = when (lastException) {
                is UnknownHostException -> "Unknown host: $address"
                is IOException -> "Network error: ${lastException.message}"
                else -> lastException?.message ?: "Unknown error"
            },
            lastChecked = System.currentTimeMillis()
        )
    }
}
