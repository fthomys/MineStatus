package me.fthomys.minestatus.data.network

import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path

/**
 * Retrofit API interface for the mcsrvstat.us API.
 * API documentation: https://api.mcsrvstat.us/
 */
interface McSrvStatApi {
    
    /**
     * Get the status of a Java Edition Minecraft server.
     * 
     * @param address The server address (can include port, e.g., "example.com:25565")
     * @return The server status response
     */
    @Headers("User-Agent: MineStatus/1.0")
    @GET("3/{address}")
    suspend fun getJavaServerStatus(@Path("address") address: String): McSrvStatResponse
    
    /**
     * Get the status of a Bedrock Edition Minecraft server.
     * 
     * @param address The server address (can include port, e.g., "example.com:19132")
     * @return The server status response
     */
    @Headers("User-Agent: MineStatus/1.0")
    @GET("bedrock/3/{address}")
    suspend fun getBedrockServerStatus(@Path("address") address: String): McSrvStatResponse
    
    companion object {
        const val BASE_URL = "https://api.mcsrvstat.us/"
    }
}