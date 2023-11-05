package com.example.playlistmaker.data.storage.db.dto

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "playlists_table")
data class PlaylistDb(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val name: String,
    val description: String,
    val photoPath: String,
    val trackIdsList: String,
)