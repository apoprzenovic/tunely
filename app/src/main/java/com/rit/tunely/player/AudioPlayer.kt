package com.rit.tunely.player

interface AudioPlayer {
    fun play(url: String)
    fun stop()
    fun release()
}