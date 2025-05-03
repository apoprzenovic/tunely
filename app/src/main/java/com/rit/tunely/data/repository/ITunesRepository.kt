package com.rit.tunely.data.repository

import com.rit.tunely.data.model.Track
import com.rit.tunely.data.remote.ITunesApi
import com.rit.tunely.data.remote.dto.toDomain
import com.rit.tunely.util.Constants
import com.rit.tunely.util.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject
import kotlin.random.Random

class ITunesRepository @Inject constructor(
    private val api: ITunesApi
) {
    private val searchChars = ('a'..'z').toList()

    suspend fun getRandomTrackWithPreview(maxRetries: Int = 2): Resource<Track> = withContext(Dispatchers.IO) {
        repeat(maxRetries + 1) { attempt ->
            try {
                val randomChar = searchChars.random().toString()
                val randomOffset = Random.nextInt(Constants.ITUNES_MAX_OFFSET + 1)

                api.searchSong(randomChar, randomOffset)
                    .results
                    .firstOrNull()
                    ?.toDomain()
                    ?.let { return@withContext Resource.Success(it) }
            } catch (e: HttpException) {
                if (attempt < maxRetries) return@repeat
                return@withContext Resource.Error(
                    "iTunes API Error: ${e.code()} – ${e.message()}"
                )
            } catch (e: IOException) {
                return@withContext Resource.Error("Network Error: ${e.localizedMessage}")
            } catch (e: Exception) {
                return@withContext Resource.Error("Unexpected error: ${e.localizedMessage}")
            }
        }
        Resource.Error("Could not find a track!")
    }
}