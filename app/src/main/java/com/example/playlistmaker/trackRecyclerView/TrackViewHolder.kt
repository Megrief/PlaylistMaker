package com.example.playlistmaker.trackRecyclerView

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.playlistmaker.R

class TrackViewHolder(private val parentView: View) : RecyclerView.ViewHolder(parentView) {
    private val trackName: TextView = parentView.findViewById(R.id.track_name) as TextView
    private val artistName: TextView = parentView.findViewById(R.id.artist_name) as TextView
    private val trackTime: TextView = parentView.findViewById(R.id.track_time) as TextView
    private val poster: ImageView = parentView.findViewById(R.id.poster) as ImageView

    fun bind(track: Track) {
        trackName.text = track.trackName
        artistName.text = track.artistName
        trackTime.text = track.getLength()
        Glide.with(parentView).load(track.artworkUrl100).placeholder(R.drawable.placeholder).into(poster)
    }

}