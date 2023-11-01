package com.example.playlistmaker.data.storage.db.dto

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "playlists_table")
data class PlaylistDb(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val name: String,
    val description: String,
    val photoPath: String,
    val trackIdsList: String,
    val addingDate: Long
)