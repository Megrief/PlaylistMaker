package com.example.playlistmaker.ui.audioplayer.view_model.player

import android.media.MediaPlayer

class Player(
    private val player: MediaPlayer
) {

    fun configurePlayer(
        url: String,
        onPrepared: () -> Unit,
        onCompleted: () -> Unit
    ) {
        player.setDataSource(url)
        player.setOnPreparedListener { onPrepared() }
        player.setOnCompletionListener { onCompleted() }
        player.prepareAsync()
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