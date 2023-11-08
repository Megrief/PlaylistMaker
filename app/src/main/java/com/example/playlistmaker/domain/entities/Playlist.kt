package com.example.playlistmaker.domain.entities

data class Playlist(
    override val id: Long = 0,
    val name: String,
    val photoId: Long,
    val description: String,
    val trackIdsList: MutableList<Long> = mutableListOf()
) : EntityRoot
