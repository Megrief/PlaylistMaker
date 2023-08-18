package com.example.playlistmaker.ui.search.activity.adapter

import androidx.recyclerview.widget.DiffUtil
import com.example.playlistmaker.domain.entities.Track

class TrackListCallback(private val oldList: List<Track>, private val newList: List<Track>) : DiffUtil.Callback() {
    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] === newList[newItemPosition]
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }
}