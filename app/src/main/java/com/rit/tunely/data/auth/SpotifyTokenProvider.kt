package com.rit.tunely.data.auth

import com.rit.tunely.BuildConfig
import com.rit.tunely.data.remote.SpotifyAuthService
import com.rit.tunely.util.Constants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object SpotifyTokenProvider {

    private val authApi: SpotifyAuthService by lazy {
        Retrofit.Builder()
            .baseUrl(Constants.SPOTIFY_BASE_AUTH_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(SpotifyAuthService::class.java)
    }

    private var cachedTokenValue: String? = null
    private var expiry: Long = 0L

    suspend fun getTokenValue(): Result<String> = withContext(Dispatchers.IO) {
        val now = System.currentTimeMillis()
        if (cachedTokenValue != null && now < expiry) {
            return@withContext Result.success(cachedTokenValue!!)
        }

        val id = BuildConfig.SPOTIFY_CLIENT_ID
        val secret = BuildConfig.SPOTIFY_CLIENT_SECRET
        if (id.isBlank() || secret.isBlank()) {
            val errorMsg = "Missing or placeholder client credentials in BuildConfig."
            return@withContext Result.failure(IllegalStateException(errorMsg))
        }

        return@withContext try {
            val r = authApi.getTokenWithFormCredentials(
                clientId = id,
                clientSecret = secret
            )
            cachedTokenValue = r.access_token
            expiry = now + r.expires_in * 1000L
            Result.success(cachedTokenValue!!)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}