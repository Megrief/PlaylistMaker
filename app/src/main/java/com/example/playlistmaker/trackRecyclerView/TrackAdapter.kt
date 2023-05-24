package com.example.playlistmaker.trackRecyclerView

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R

class TrackAdapter : RecyclerView.Adapter<TrackViewHolder>() {
    var trackList: MutableList<Track> = mutableListOf()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        return TrackViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.track_card, parent, false))
    }

    override fun getItemCount(): Int {
        return trackList.size
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.bind(trackList[position])
    }
}