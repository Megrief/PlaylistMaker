package com.example.playlistmaker.domain.entity

data class Track(
    val trackId: Long,
    val trackName: String,
    val artistName: String,
    val trackTime: Long,
    val artworkUrl100: String,
    val collectionName: String,
    val country: String,
    val releaseDate: String,
    val primaryGenreName: String,
    val previewUrl: String
)