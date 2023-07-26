package com.example.playlistmaker.presentaion.ui.audioplayer

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.entities.Track

object AudioplayerBinder {

    fun bind(activity: AudioplayerActivity, track: Track) {
        val poster = activity.findViewById<ImageView>(R.id.poster)
        val cornerSizeInPx = activity.resources.getDimensionPixelSize(R.dimen.small)
        if (track.artworkUrl100.isNotEmpty()) {
            Glide.with(activity)
                .load(getPoster512(track.artworkUrl100))
                .placeholder(R.drawable.placeholder)
                .transform(CenterCrop(), RoundedCorners(cornerSizeInPx))
                .into(poster)
        }

        with(activity) {
            bindItemText(findViewById(R.id.track_name), track.trackName, findViewById(R.id.track_name))
            bindItemText(findViewById(R.id.artist_name), track.artistName, findViewById(R.id.artist_name))
            bindItemText(findViewById(R.id.track_length_value), track.trackTime, findViewById(R.id.track_length_group))
            bindItemText(findViewById(R.id.collection_value), track.collectionName, findViewById(R.id.collection_group))
            bindItemText(findViewById(R.id.release_year_value), track.releaseDate, findViewById(R.id.release_year_group))
            bindItemText(findViewById(R.id.genre_value), track.primaryGenreName, findViewById(R.id.genre_group))
            bindItemText(findViewById(R.id.country_value), track.country, findViewById(R.id.country_group))
        }
    }

    private fun bindItemText(view: TextView, text: String, group: View) {
        if (isNotEmpty(text, group)) view.text = text
    }

    private fun isNotEmpty(str: String, group: View): Boolean {
        return if (str.isBlank()) {
            group.visibility = View.GONE
            false
        } else true
    }

    private fun getPoster512(url: String): String = url.replaceAfterLast('/', "512x512bb.jpg")


}