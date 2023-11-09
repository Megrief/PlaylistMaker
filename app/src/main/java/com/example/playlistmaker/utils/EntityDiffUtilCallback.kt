package com.example.playlistmaker.utils

import androidx.recyclerview.widget.DiffUtil
import com.example.playlistmaker.domain.entities.EntityRoot

class EntityDiffUtilCallback(private val oldList: List<EntityRoot>, private val newList: List<EntityRoot>) : DiffUtil.Callback() {
    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] === newList[newItemPosition]
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].id == newList[newItemPosition].id
    }
}