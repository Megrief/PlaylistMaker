package com.example.playlistmaker.data.db.dto

import androidx.room.ColumnInfo
import androidx.room.ColumnInfo.Companion.INTEGER
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favourite_tracks_table")
data class TrackDb(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(typeAffinity = INTEGER)
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
    val addingDate: Long
)