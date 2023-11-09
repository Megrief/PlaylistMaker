package com.example.playlistmaker.ui.audioplayer.view_model.player

import android.media.MediaPlayer
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

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

    fun play() {
        player.start()
    }

    fun getCurrentPosition(): Flow<Int> {
        return flow {
            while (true) {
                emit(player.currentPosition)
                delay(DELAY_MILLIS)
            }
        }
    }

    fun pause() {
        player.pause()
    }

    companion object {
        const val DELAY_MILLIS = 300L
    }
}