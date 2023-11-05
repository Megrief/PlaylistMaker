package com.example.playlistmaker.ui.playlist_creation.screen_state

import java.io.File

data class PlaylistCreationScreenState(
    val playlistName: String,
    val playlistDescription: String,
    val playlistPhoto: File?,
    val notEmpty: Boolean
)
