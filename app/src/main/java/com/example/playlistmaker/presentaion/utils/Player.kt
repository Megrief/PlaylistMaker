package com.example.playlistmaker.presentaion.utils

import android.media.MediaPlayer

class Player(
    url: String,
    onPrepared: () -> Unit,
    onCopletion: () -> Unit
) {
    private val player = MediaPlayer()
    private var playerState = STATE_DEFAULT


    init {
        player.setDataSource(url)
        player.prepareAsync()
        player.setOnPreparedListener() {
            playerState = STATE_PREPARED
            onPrepared()
        }
        player.setOnCompletionListener {
            playerState = STATE_PREPARED
            onCopletion()
        }
    }

    fun playbackControl() {
        when (playerState) {
            STATE_PREPARED, STATE_PAUSED -> playMedia()
            STATE_PLAYING -> pauseMedia()
        }
    }

    fun releaseResources() {
        player.release()
        playerState = STATE_DEFAULT
    }

    fun getCurrentPosition(): Int = player.currentPosition

    private fun playMedia() {
        player.start()
        playerState = STATE_PLAYING
    }

    private fun pauseMedia() {
        player.pause()
        playerState = STATE_PAUSED
    }

    companion object {
        private const val STATE_DEFAULT = 0
        private const val STATE_PREPARED = 1
        private const val STATE_PLAYING = 2
        private const val STATE_PAUSED = 3
    }
}