package com.example.playlistmaker.utils

import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
import android.widget.ImageButton
import android.widget.TextView
import com.example.playlistmaker.R
import java.text.SimpleDateFormat
import java.util.Locale

class Player(
    url: String,
    private val timer: TextView,
    private val playPauseButton: ImageButton
    ) {
    private val player = MediaPlayer()
    private var playerState = STATE_DEFAULT
    private val mainHandler = Handler(Looper.getMainLooper())
    private val counter = object : Runnable {
        override fun run() {
            val position = SimpleDateFormat("mm:ss", Locale.getDefault()).format(player.currentPosition)
            timer.text = position
            mainHandler.postDelayed(this, DELAY)
        }
    }

    init {
        player.setDataSource(url)
        player.prepareAsync()
        playerState = STATE_PREPARED
        player.setOnCompletionListener {
            playerState = STATE_PREPARED
            timer.setText(R.string.time_left)
            playPauseButton.setImageResource(R.drawable.play_icon)
            mainHandler.removeCallbacks(counter)
        }
    }

    fun playbackControl() {
        when (playerState) {
            STATE_PREPARED, STATE_PAUSED -> playMedia()
            STATE_PLAYING -> pauseMedia()
        }
    }

    private fun playMedia() {
        playPauseButton.setImageResource(R.drawable.pause_icon)
        player.start()
        mainHandler.postDelayed(counter, DELAY)
        playerState = STATE_PLAYING
    }

    fun pauseMedia() {
        playPauseButton.setImageResource(R.drawable.play_icon)
        player.pause()
        mainHandler.removeCallbacks(counter)
        playerState = STATE_PAUSED
    }

    fun releaseResources() {
        player.release()
        playerState = STATE_DEFAULT
    }

    companion object {
        private const val STATE_DEFAULT = 0
        private const val STATE_PREPARED = 1
        private const val STATE_PLAYING = 2
        private const val STATE_PAUSED = 3
        private const val DELAY = 333L
    }
}