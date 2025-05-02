package com.rit.tunely.data.remote

import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface SpotifyAuthService {
    @FormUrlEncoded
    @POST("api/token")
    suspend fun getTokenWithFormCredentials(
        @Field("grant_type") grantType: String = "client_credentials",
        @Field("client_id") clientId: String,
        @Field("client_secret") clientSecret: String,
    ): TokenResponse
}

data class TokenResponse(
    val access_token: String,
    val token_type: String,
    val expires_in: Int
)