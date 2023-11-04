package com.example.playlistmaker.domain.entities

import java.io.File

data class Playlist(
    override val id: Long = 0,
    val name: String,
    val photoFile: File?,
    val description: String,
    val trackIdsList: MutableList<Long> = mutableListOf()
) : EntityRoot