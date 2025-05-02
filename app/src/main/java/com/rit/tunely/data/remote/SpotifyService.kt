package com.rit.tunely.data.remote

import com.rit.tunely.data.remote.dto.TrackSearchResponse
import com.rit.tunely.util.Constants
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface SpotifyService {

    @GET("v1/search")
    suspend fun searchTracks(
        @Header("Authorization") authHeader: String,
        @Query("q") query: String,
        @Query("limit") limit: Int = 1,
        @Query("type") type: String = "track",
        @Query("offset") offset: Int = 0
    ): TrackSearchResponse

    companion object {
        fun create(): SpotifyService {
            return Retrofit.Builder()
                .baseUrl(Constants.SPOTIFY_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(SpotifyService::class.java)
        }
    }
}