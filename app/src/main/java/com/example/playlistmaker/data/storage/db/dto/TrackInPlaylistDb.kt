package com.example.playlistmaker.data.storage.db.dto

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "track_in_playlists_table")
data class TrackInPlaylistDb(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "trackId", typeAffinity = ColumnInfo.INTEGER)
    val trackId: Long,
    val trackName: String,
    val artistName: String,
    val trackTime: Long,
    val artworkUrl100: String,
    val collectionName: String,
    val country: String,
    val releaseDate: String,
    val primaryGenreName: String,
    val previewUrl: String,
)
