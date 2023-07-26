package com.example.playlistmaker.presentaion.ui.audioplayer

import android.os.Handler
import android.os.Looper
import android.widget.ImageButton
import android.widget.TextView
import com.example.playlistmaker.R
import com.example.playlistmaker.presentaion.utils.Player
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerInteractor(
    url: String,
    private val timer: TextView,
    private val playButton: ImageButton
) {
    private lateinit var player: Player
    private val mainHandler = Handler(Looper.getMainLooper())
    private var isPlaying = false
    private val counter = object : Runnable {
        override fun run() {
            val position = player.getCurrentPosition()
            timer.text = SimpleDateFormat("mm:ss", Locale.getDefault())
                .format(position)
            mainHandler.postDelayed(this, DELAY)
        }
    }

    init {
        playButtonConfig()
        player = Player(
            url,
            onPrepared = { playButton.isClickable = true },
            onCopletion = {
                onPause()
                timer.setText(R.string.time_left)
            }
        )
    }

    private fun onPlay() {
        isPlaying = true
        playButton.setImageResource(R.drawable.pause_icon)
        mainHandler.postDelayed(counter, DELAY)
    }

    fun onPause() {
        isPlaying = false
        playButton.setImageResource(R.drawable.play_icon)
        mainHandler.removeCallbacks(counter)
    }

    fun onRelease() {
        if (isPlaying) mainHandler.removeCallbacks(counter)
        player.releaseResources()
    }

    private fun playButtonConfig() {
        playButton.setOnClickListener {
            player.playbackControl()
            if (isPlaying) onPause() else onPlay()
        }
    }

    companion object {
        private const val DELAY = 333L
    }
}