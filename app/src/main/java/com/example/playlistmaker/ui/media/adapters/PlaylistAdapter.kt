package com.example.playlistmaker.ui.media.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.databinding.PlaylistCardBinding
import com.example.playlistmaker.domain.entities.Playlist
import com.example.playlistmaker.utils.EntityDiffUtilCallback

class PlaylistAdapter(private val onPlaylistClick: (Playlist) -> Unit) : RecyclerView.Adapter<PlaylistViewHolder>() {

    val contentList: MutableList<Playlist> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistViewHolder {
        val binding = PlaylistCardBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return PlaylistViewHolder(binding)
    }

    override fun getItemCount(): Int = contentList.size

    override fun onBindViewHolder(holder: PlaylistViewHolder, position: Int) {
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