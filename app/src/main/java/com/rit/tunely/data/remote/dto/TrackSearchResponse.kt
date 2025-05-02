package com.rit.tunely.data.remote.dto

import com.google.gson.annotations.SerializedName

data class TrackSearchResponse(@SerializedName("tracks") val tracks: TracksDto)
data class TracksDto(@SerializedName("items") val items: List<TrackDto>)
data class TrackDto(
    @SerializedName("id") val id: String,
    @SerializedName("name") val name: String,
    @SerializedName("preview_url") val previewUrl: String?,
    @SerializedName("artists") val artists: List<ArtistDto>
)

data class ArtistDto(@SerializedName("name") val name: String)
