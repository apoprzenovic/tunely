package com.rit.tunely.data.remote

import com.rit.tunely.data.model.Track
import com.rit.tunely.data.remote.dto.TrackDto

fun TrackDto.toDomain() = Track(
    id = id,
    title = name,
    artist = artists.firstOrNull()?.name ?: "Unknown",
    previewUrl = previewUrl
)
