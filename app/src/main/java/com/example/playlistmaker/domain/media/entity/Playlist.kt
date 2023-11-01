package com.example.playlistmaker.domain.media.entity

data class Playlist(
    val id: Int = 0,
    val name: String,
    val photoFileName: String,
    val description: String,
    val trackIdsList: List<Long> = emptyList()
)