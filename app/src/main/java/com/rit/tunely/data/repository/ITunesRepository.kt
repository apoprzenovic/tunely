package com.rit.tunely.data.repository

import com.rit.tunely.data.model.Track
import com.rit.tunely.data.remote.ITunesApi
import com.rit.tunely.data.remote.dto.SongDto
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

    suspend fun getRandomTrackWithPreview(
        maxRetries: Int = 2
    ): Resource<Track> = withContext(Dispatchers.IO) {
        repeat(maxRetries + 1) { attempt ->
            val outcome = runCatching { fetchAndPick() }
            outcome.getOrNull()?.let { return@withContext Resource.Success(it) }
            if (attempt == maxRetries) {
                val e = outcome.exceptionOrNull()
                return@withContext when (e) {
                    is HttpException -> Resource.Error("iTunes API Error: ${e.code()} – ${e.message()}")
                    is IOException -> Resource.Error("Network Error: ${e.localizedMessage}")
                    else -> Resource.Error(e?.localizedMessage ?: "Unexpected error")
                }
            }
        }
        Resource.Error("Could not find a suitable track!")
    }

    private suspend fun fetchAndPick(): Track {
        val term = searchChars.random().toString()
        val offset = Random.nextInt(Constants.ITUNES_MAX_OFFSET + 1)
        val batch = api.searchSong(term, offset).results
        return pickCandidate(batch) ?: error("No <=9-char title in batch")
    }

    private fun pickCandidate(candidates: List<SongDto>): Track? = candidates
        .mapNotNull { it.toDomain() }
        .firstOrNull { it.title.length <= 9 }
}
