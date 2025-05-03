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

    suspend fun getRandomTrackWithPreview(): Resource<Track> = withContext(Dispatchers.IO) {
        try {
            val randomChar = searchChars.random().toString()
            val randomOffset = Random.nextInt(Constants.ITUNES_MAX_OFFSET + 1)

            val response = api.searchSong(
                term = randomChar,
                offset = randomOffset,
                limit = 1
            )

            if (response.resultCount > 0) {
                val trackDto = response.results.first()
                val track = trackDto.toDomain()
                if (track != null) {
                    return@withContext Resource.Success(track)
                }
            }
        } catch (e: HttpException) {
            return@withContext Resource.Error("iTunes API Error: ${e.code()} - ${e.message()}")
        } catch (e: IOException) {
            return@withContext Resource.Error("Network Error: ${e.localizedMessage}")
        } catch (e: Exception) {
            return@withContext Resource.Error("An unexpected error occurred: ${e.localizedMessage}")
        }

        Resource.Error("Could not find a track!")
    }
}