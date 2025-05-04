package com.rit.tunely.player

import kotlinx.coroutines.flow.StateFlow

interface AudioPlayer {
    val isPlaying: StateFlow<Boolean>
    fun play(url: String)
    fun stop()
    fun release()
}