package com.example.playlistmaker.domain.entities

data class Track(
    override val id: Long,
    val trackName: String,
    val artistName: String,
    val trackTime: Long,
    val artworkUrl100: String,
    val collectionName: String,
    val country: String,
    val releaseDate: String,
    val primaryGenreName: String,
    val previewUrl: String
) : EntityRoot