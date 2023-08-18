package com.example.playlistmaker.ui.search.activity.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.databinding.TrackCardBinding
import com.example.playlistmaker.domain.entities.Track

class TrackAdapter(private val onTrackClicked: (Track) -> Unit) : RecyclerView.Adapter<TrackViewHolder>() {
    private val trackList: MutableList<Track> = mutableListOf()

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

    fun setTrackList(newTrackList: List<Track>) {
        val diffCallback = TrackListCallback(trackList, newTrackList)
        val diffTracks = DiffUtil.calculateDiff(diffCallback)
        trackList.clear()
        trackList.addAll(newTrackList)
        diffTracks.dispatchUpdatesTo(this)
    }
}