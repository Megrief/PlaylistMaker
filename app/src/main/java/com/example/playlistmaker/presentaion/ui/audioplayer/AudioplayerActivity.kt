package com.example.playlistmaker.presentaion.ui.audioplayer

import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isGone
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.entities.Track
import com.example.playlistmaker.presentaion.ui.search.SearchActivity
import com.example.playlistmaker.presentaion.utils.Creator
import com.google.android.material.appbar.MaterialToolbar
import java.util.Locale

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
                bind(track)

                if (track.previewUrl.isNotEmpty()) {
                    playerInteractor = PlayerInteractor(
                        track.previewUrl,
                        timer = findViewById(R.id.playing_time),
                        playButton = findViewById(R.id.play_button)
                    )
                } else {
                    Toast.makeText(this, "Нет ознакомительного фрагмента", Toast.LENGTH_LONG).show()
                }
            }
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

    private fun bind(track: Track) {
        val poster = findViewById<ImageView>(R.id.poster)
        val cornerSizeInPx = resources.getDimensionPixelSize(R.dimen.small)
        if (track.artworkUrl100.isNotEmpty()) {
            Glide.with(this)
                .load(getPoster512(track.artworkUrl100))
                .placeholder(R.drawable.placeholder)
                .transform(CenterCrop(), RoundedCorners(cornerSizeInPx))
                .into(poster)
        }
        bindItemText(findViewById(R.id.track_name), track.trackName, findViewById(R.id.track_name))
        bindItemText(findViewById(R.id.artist_name), track.artistName, findViewById(R.id.artist_name))
        bindItemText(findViewById(R.id.track_length_value), getLength(track.trackTime), findViewById(R.id.track_length_group))
        bindItemText(findViewById(R.id.collection_value), track.collectionName, findViewById(R.id.collection_group))
        bindItemText(findViewById(R.id.release_year_value), track.releaseDate, findViewById(R.id.release_year_group))
        bindItemText(findViewById(R.id.genre_value), track.primaryGenreName, findViewById(R.id.genre_group))
        bindItemText(findViewById(R.id.country_value), track.country, findViewById(R.id.country_group))
    }

    private fun bindItemText(view: TextView, text: String, group: View) {
        if (isNotEmpty(text, group)) view.text = text
    }

    private fun isNotEmpty(str: String, group: View): Boolean {
        return if (str.isBlank()) {
            group.isGone = true
            false
        } else true
    }

    private fun getPoster512(url: String): String = url.replaceAfterLast('/', "512x512bb.jpg")

    private fun getLength(time: Long): String = SimpleDateFormat("mm:ss", Locale.getDefault()).format(time)
}