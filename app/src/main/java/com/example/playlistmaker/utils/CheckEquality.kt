package com.example.playlistmaker.utils

import com.example.playlistmaker.domain.entities.EntityRoot

fun isEquals(oldList: List<EntityRoot>, newList: List<EntityRoot>): Boolean {
    return if (oldList.size != newList.size) {
        false
    } else {
        for (index in oldList.indices) {
            if (oldList[index].id != newList[index].id) return false
        }
        true
    }
}