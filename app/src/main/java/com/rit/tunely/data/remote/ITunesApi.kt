package com.rit.tunely.data.remote

import com.rit.tunely.data.remote.dto.ITunesResponse
import com.rit.tunely.util.Constants
import retrofit2.http.GET
import retrofit2.http.Query

interface ITunesApi {

    /**
     * Searches the iTunes Store for songs.
     * @param term The search term (e.g., a random character).
     * @param offset The offset for pagination (used for randomness).
     * @param limit The maximum number of results to return.
     * @param entity The type of entity to search for (e.g., "song").
     * @param media The type of media (e.g., "music").
     */
    @GET("search")
    suspend fun searchSong(
        @Query("term") term: String,
        @Query("offset") offset: Int,
        @Query("limit") limit: Int = Constants.TRACK_LIMIT,
        @Query("entity") entity: String = "song",
        @Query("media") media: String = "music",
    ): ITunesResponse
}