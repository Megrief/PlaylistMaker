package com.example.playlistmaker.ui.audioplayer.view_model

import com.example.playlistmaker.domain.entities.Playlist
import com.example.playlistmaker.domain.entities.Track

sealed class AudioplayerScreenState {
    object Loading : AudioplayerScreenState()
    data class Content(val track: Track, val inFavourite: Boolean, val playlists: List<Playlist>) : AudioplayerScreenState()
    object Error : AudioplayerScreenState()
}
