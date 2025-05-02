package com.rit.tunely.data.repository

import com.rit.tunely.data.auth.SpotifyTokenProvider
import com.rit.tunely.data.model.Track
import com.rit.tunely.data.remote.SpotifyService
import com.rit.tunely.data.remote.toDomain
import com.rit.tunely.util.Resource
import javax.inject.Inject
import kotlin.random.Random

class SpotifyRepository @Inject constructor(
    private val api: SpotifyService
) {
    private val MAX_OFFSET = 999

    private fun generateRandomQuery(): String {
        val characters = "abcdefghijklmnopqrstuvwxyz"
        val randomChar = characters[Random.nextInt(characters.length)]
        return when (Random.nextInt(2)) {
            0 -> "$randomChar*"
            else -> "*$randomChar*"
        }
    }

    suspend fun getRandomTrack(): Resource<Track> {
        val tokenRes = SpotifyTokenProvider.getTokenValue()

        if (tokenRes.isFailure) {
            val msg = tokenRes.exceptionOrNull()?.localizedMessage ?: "Auth failed"
            return Resource.Error(msg)
        }

        val tokenValue = tokenRes.getOrNull()!!
        val authHeader = "Bearer $tokenValue"

        return try {
            val randomQuery = generateRandomQuery()

            val initialSearchResponse = api.searchTracks(
                authHeader = authHeader,
                query = randomQuery,
                limit = 1,
                offset = 0
            )
            val totalTracks = initialSearchResponse.tracks.items.size

            if (totalTracks == 0) {
                return Resource.Error("No tracks found for the random query.")
            }

            val maxPossibleOffset = minOf(totalTracks - 1, MAX_OFFSET)
            val randomOffset = if (maxPossibleOffset > 0) Random.nextInt(maxPossibleOffset + 1) else 0

            val finalSearchResponse = api.searchTracks(
                authHeader = authHeader,
                query = randomQuery,
                limit = 1,
                offset = randomOffset
            )

            val randomTrackDto = finalSearchResponse.tracks.items.firstOrNull()

            if (randomTrackDto != null) {
                Resource.Success(randomTrackDto.toDomain())
            } else {
                Resource.Error("Could not retrieve a track at the calculated offset.")
            }

        } catch (e: Exception) {
            val errorMsg = if (e is retrofit2.HttpException) {
                "Spotify API Error: ${e.code()} - ${e.message()}"
            } else {
                e.localizedMessage ?: "Spotify search error"
            }
            Resource.Error(errorMsg)
        }
    }
}