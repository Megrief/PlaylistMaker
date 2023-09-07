package com.example.playlistmaker.domain.media.entity

import com.example.playlistmaker.domain.entity.Track

data class Playlist(
    val id: Int,
    val name: String,
    val trackList: List<Track>
)