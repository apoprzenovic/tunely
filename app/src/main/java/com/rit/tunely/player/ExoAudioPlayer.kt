package com.rit.tunely.player

import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

class ExoAudioPlayer @Inject constructor(
    private val player: Player
) : AudioPlayer {

    private val _isPlaying = MutableStateFlow(false)
    override val isPlaying: StateFlow<Boolean> = _isPlaying.asStateFlow()

    private val listener = object : Player.Listener {
        override fun onIsPlayingChanged(isPlaying: Boolean) {
            _isPlaying.value = isPlaying
        }

        override fun onPlaybackStateChanged(playbackState: Int) {
            if (playbackState == Player.STATE_ENDED) {
                _isPlaying.value = false
            }
        }
    }

    init {
        player.addListener(listener)
    }

    override fun play(url: String) {
        if (url.isBlank()) return

        val mediaItem = MediaItem.fromUri(url)
        player.setMediaItem(mediaItem)
        player.prepare()
        player.playWhenReady = true
    }

    override fun stop() {
        if (player.isPlaying) {
            player.pause()
        }

        if (!_isPlaying.value) {
            _isPlaying.value = false
        }
    }

    override fun release() {
        player.removeListener(listener)
        player.release()
        _isPlaying.value = false
    }
}