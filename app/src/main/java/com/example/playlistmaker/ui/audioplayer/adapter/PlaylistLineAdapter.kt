package com.example.playlistmaker.ui.audioplayer.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.databinding.PlaylistLineBinding
import com.example.playlistmaker.domain.entities.Playlist
import com.example.playlistmaker.utils.EntityDiffUtilCallback

class PlaylistLineAdapter(private val onPlaylistClick: (Playlist) -> Unit) : RecyclerView.Adapter<PlaylistLineViewHolder>() {

    val contentList: MutableList<Playlist> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistLineViewHolder {
        val binding = PlaylistLineBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return PlaylistLineViewHolder(binding)
    }

    override fun getItemCount(): Int = contentList.size

    override fun onBindViewHolder(holder: PlaylistLineViewHolder, position: Int) {
        holder.bind(playlist = contentList[position])
        holder.itemView.setOnClickListener {
            onPlaylistClick(contentList[position])
        }
    }

    fun setContent(newList: List<Playlist>) {
        val diffCallback = EntityDiffUtilCallback(contentList, newList)
        val diffTracks = DiffUtil.calculateDiff(diffCallback)
        contentList.clear()
        contentList.addAll(newList)
        diffTracks.dispatchUpdatesTo(this)
    }
}