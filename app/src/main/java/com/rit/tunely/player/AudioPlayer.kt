package com.rit.tunely.player


import android.content.Context
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class AudioPlayer @Inject constructor(@ApplicationContext ctx: Context) {
    private val player = ExoPlayer.Builder(ctx).build()
    fun play(url: String) {
        player.setMediaItem(MediaItem.fromUri(url)); player.prepare(); player.play()
    }

    fun stop() {
        player.stop()
    }

    fun release() {
        player.release()
    }
}
