package com.example.playlistmaker.ui.audioplayer.vm.player

import android.media.MediaPlayer

class Player(
    url: String,
    onPrepared: () -> Unit,
    onCompeted: () -> Unit
) {
    private val player = MediaPlayer()

    init {
        player.setDataSource(url)
        player.prepareAsync()
        player.setOnPreparedListener { onPrepared() }
        player.setOnCompletionListener { onCompeted() }
    }

    fun releaseResources() {
        player.release()
    }

    fun getCurrentPosition(): Int = player.currentPosition

    fun play() {
        player.start()
    }

    fun pause() {
        player.pause()
    }
}