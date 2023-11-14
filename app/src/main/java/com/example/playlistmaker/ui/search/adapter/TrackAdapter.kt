package com.example.playlistmaker.ui.search.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.databinding.TrackCardBinding
import com.example.playlistmaker.domain.entities.Track
import com.example.playlistmaker.utils.EntityDiffUtilCallback

class TrackAdapter(
    private val onTrackClicked: (Track) -> Unit,
    private val onTrackLongClicked: (Track) -> Boolean = { false }
) : RecyclerView.Adapter<TrackViewHolder>() {
    private val _trackList: MutableList<Track> = mutableListOf()

    val trackList: List<Track>
        get() = _trackList

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val layoutInspector = LayoutInflater.from(parent.context)
        return TrackViewHolder(TrackCardBinding.inflate(layoutInspector, parent, false))
    }

    override fun getItemCount(): Int {
        return _trackList.size
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        val track = _trackList[position]
        holder.bind(track)
        holder.itemView.setOnClickListener { onTrackClicked(track) }
        holder.itemView.setOnLongClickListener { onTrackLongClicked(track) }
    }

    fun setTrackList(newTrackList: List<Track>) {
        val diffCallback = EntityDiffUtilCallback(_trackList, newTrackList)
        val diffTracks = DiffUtil.calculateDiff(diffCallback)
        _trackList.clear()
        _trackList.addAll(newTrackList)
        diffTracks.dispatchUpdatesTo(this)
    }
}