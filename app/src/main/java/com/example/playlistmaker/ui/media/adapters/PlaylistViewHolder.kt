package com.example.playlistmaker.ui.media.adapters

import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.PlaylistCardBinding
import com.example.playlistmaker.domain.entities.Playlist

class PlaylistViewHolder(private val binding: PlaylistCardBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(playlist: Playlist) {
        with(binding) {
            val cornerSizeInPx = root.resources.getDimensionPixelSize(R.dimen.small)
            Glide.with(root)
                .load(playlist.photoFile?.toUri())
                .placeholder(R.drawable.placeholder)
                .transform(CenterCrop(), RoundedCorners(cornerSizeInPx))
                .into(playlistPhoto)
            playlistName.text = playlist.name
            val size = playlist.trackIdsList.size.toString()
            val text = size + " " + getCorrect(size)
            playlistSize.text = text
        }
    }

    private fun getCorrect(num: String): String {
        return when {
            num.last() == '1'
                    && if (num.length > 1) num[num.lastIndex - 1] != '1' else true -> "трек"
            num.last() in '2'..'4'
                    && if (num.length > 1) num[num.lastIndex - 1] != '1' else true -> "трека"
            else -> "треков"
        }
    }

}