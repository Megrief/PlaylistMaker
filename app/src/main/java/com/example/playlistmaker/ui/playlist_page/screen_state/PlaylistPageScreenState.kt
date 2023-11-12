package com.example.playlistmaker.ui.playlist_page.screen_state

import com.example.playlistmaker.domain.entities.Playlist
import com.example.playlistmaker.domain.entities.Track

sealed class PlaylistPageScreenState {
    object Loading : PlaylistPageScreenState()
    object Error : PlaylistPageScreenState()
    data class Content(
        val playlist: Playlist?,
        val trackList: List<Track>,
        val totalLength: String,
        val totalTracks: String
    ) : PlaylistPageScreenState()
}