package com.example.playlistmaker.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.databinding.TrackCardBinding
import com.example.playlistmaker.domain.entities.Track

class TrackAdapter(private val onTrackClicked: (Track) -> Unit) : RecyclerView.Adapter<TrackViewHolder>() {
    var trackList: MutableList<Track> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val layoutInspector = LayoutInflater.from(parent.context)
        return TrackViewHolder(TrackCardBinding.inflate(layoutInspector, parent, false))
    }

    override fun getItemCount(): Int {
        return trackList.size
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        val track = trackList[position]
        holder.bind(track)
        holder.itemView.setOnClickListener { onTrackClicked(track) }
    }
}