package com.example.playlistmaker.presentaion.ui.audioplayer

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.entities.Track
import com.example.playlistmaker.presentaion.ui.search.SearchActivity
import com.example.playlistmaker.presentaion.utils.Creator
import com.google.android.material.appbar.MaterialToolbar

class AudioplayerActivity : AppCompatActivity() {
    private val toolbar by lazy { findViewById<MaterialToolbar>(R.id.toolbar) }
    private val addToMediaButton by lazy { findViewById<ImageButton>(R.id.add_to_media) }
    private val likeButton by lazy { findViewById<ImageButton>(R.id.like) }
    private val storageInteractorTrack by lazy { Creator.createStorageInteractorTrack(applicationContext) }
    private lateinit var playerInteractor: PlayerInteractor
    private lateinit var track: Track

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_audioplayer)

        storageInteractorTrack.get(SearchActivity.TRACK) {
            val mainHandler = Handler(Looper.getMainLooper())
            mainHandler.post {
                if (it != null) {
                    track = it
                } else onBackPressedDispatcher.onBackPressed()
            }
        }

        AudioplayerBinder.bind(this, track)

        if (track.previewUrl.isNotEmpty()) {
            playerInteractor = PlayerInteractor(
                track.previewUrl,
                timer = findViewById(R.id.playing_time),
                playButton = findViewById(R.id.play_button)
            )
        } else {
            Toast.makeText(this, "Нет ознакомительного фрагмента", Toast.LENGTH_LONG).show()
        }
        toolbar.setNavigationOnClickListener { onBackPressedDispatcher.onBackPressed() }
    }

    override fun onDestroy() {
        super.onDestroy()
        playerInteractor.onRelease()
    }

    override fun onPause() {
        super.onPause()
        playerInteractor.onPause()
    }
}