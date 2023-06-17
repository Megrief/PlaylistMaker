package com.example.playlistmaker.activities

import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.view.View
import android.view.View.GONE
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.trackRecyclerView.Track
import com.example.playlistmaker.utils.Constants
import com.google.gson.Gson
import java.time.LocalDateTime
import java.util.Locale

class AudioplayerActivity : AppCompatActivity() {
    private val backButton by lazy { findViewById<ImageButton>(R.id.back) }
    private val addToMediaButton by lazy { findViewById<ImageButton>(R.id.add_to_media) }
    private val playButton by lazy { findViewById<ImageButton>(R.id.play_button) }
    private val likeButton by lazy { findViewById<ImageButton>(R.id.like) }
    private val playingTime by lazy { findViewById<TextView>(R.id.playing_time) }
    private val preferences by lazy { getSharedPreferences(Constants.PLAYLIST_MAKER.key, MODE_PRIVATE) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_audioplayer)
        preferences.getString(Constants.K_TRACK.key, null)?.let {
            val track = Gson().fromJson(it, Track::class.java)
            bind(track)
        }
        backButton.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun bind(track: Track) {
        bindPoster(track.artworkUrl100.replaceAfterLast('/', "512x512bb.jpg"))
        bindText(findViewById(R.id.track_name), track.trackName, findViewById(R.id.track_name))
        bindText(findViewById(R.id.artist_name), track.artistName, findViewById(R.id.artist_name))
        bindTrackLength(track.trackTime)
        bindText(findViewById(R.id.collection_value), track.collectionName, findViewById(R.id.collection_group))
        bindReleaseYear(track.releaseDate)
        bindText(findViewById(R.id.genre_value), track.primaryGenreName, findViewById(R.id.genre_group))
        bindText(findViewById(R.id.country_value), track.country, findViewById(R.id.country_group))
    }

    private fun bindPoster(url: String) {
        val poster = findViewById<ImageView>(R.id.poster)
        val cornerSizeInPx = this.resources.getDimensionPixelSize(R.dimen.small)
        Glide.with(this)
            .load(url)
            .placeholder(R.drawable.placeholder)
            .transform(CenterCrop(), RoundedCorners(cornerSizeInPx))
            .into(poster)
    }

    private fun bindTrackLength(timeInMillis: Long) {
        val length: String = SimpleDateFormat("mm:ss", Locale.getDefault())
            .format(timeInMillis)
        findViewById<TextView>(R.id.track_length_value).text = length
    }

    private fun bindReleaseYear(releaseDateString: String) {
        if (isNotEmpty(releaseDateString, findViewById(R.id.release_year_group))) {
            val year = LocalDateTime.parse(releaseDateString.dropLast(1)).year.toString()
            findViewById<TextView>(R.id.release_year_value).text = year
        }
    }

    private fun bindText(view: TextView, text: String, group: View) {
        if (isNotEmpty(text, group)) view.text = text
    }

    private fun isNotEmpty(str: String, group: View): Boolean {
        return if (str.isBlank()) {
            group.visibility = GONE
            false
        } else true
    }

}