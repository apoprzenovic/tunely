package com.rit.tunely.data.remote.dto

import com.google.gson.annotations.SerializedName
import com.rit.tunely.data.model.Track
import com.rit.tunely.util.sanitizeTitle

data class ITunesResponse(
    @SerializedName("resultCount") val resultCount: Int,
    @SerializedName("results") val results: List<SongDto>
)

data class SongDto(
    @SerializedName("trackId") val trackId: Long,
    @SerializedName("trackName") val trackName: String?,
    @SerializedName("artistName") val artistName: String?,
    @SerializedName("previewUrl") val previewUrl: String?,
    @SerializedName("artworkUrl30") val artworkUrl30: String?,
    @SerializedName("artworkUrl60") val artworkUrl60: String?,
    @SerializedName("artworkUrl100") val artworkUrl100: String?
)

fun SongDto.toDomain(): Track? {
    if (trackName.isNullOrBlank() || artistName.isNullOrBlank() || previewUrl.isNullOrBlank()) {
        return null
    }

    val cleanedTitle = sanitizeTitle(trackName)

    if (cleanedTitle.isBlank()) {
        return null
    }

    return Track(
        id = trackId.toString(),
        title = cleanedTitle,
        artist = artistName,
        previewUrl = previewUrl,
        artworkUrl = artworkUrl100 ?: artworkUrl60 ?: artworkUrl30
    )
}