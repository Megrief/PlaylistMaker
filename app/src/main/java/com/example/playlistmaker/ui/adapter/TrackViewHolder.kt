package com.example.playlistmaker.ui.adapter

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.TrackCardBinding
import com.example.playlistmaker.domain.entities.Track
import java.text.SimpleDateFormat
import java.util.Locale

class TrackViewHolder(private val binding: TrackCardBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(track: Track) {
        with(binding) {
            trackName.text = track.trackName
            artistName.text = track.artistName
            trackTime.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(track.trackTime)

            Glide.with(root).load(track.artworkUrl100).placeholder(R.drawable.placeholder).into(poster)
        }

    }

}