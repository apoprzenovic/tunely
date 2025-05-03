package com.rit.tunely.player

import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import javax.inject.Inject

class ExoAudioPlayer @Inject constructor(
    private val player: Player
) : AudioPlayer {

    override fun play(url: String) {
        if (url.isBlank()) return

        val mediaItem = MediaItem.fromUri(url)
        player.setMediaItem(mediaItem)
        player.prepare()
        player.playWhenReady = true
    }

    override fun stop() {
        if (player.isPlaying) {
            player.stop()
        }
    }

    override fun release() {
        player.release()
    }
}