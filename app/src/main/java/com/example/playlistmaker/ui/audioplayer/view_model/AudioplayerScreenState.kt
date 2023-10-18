package com.example.playlistmaker.ui.audioplayer.view_model

import com.example.playlistmaker.domain.entity.Track

sealed class AudioplayerScreenState {
    object Loading : AudioplayerScreenState()
    data class Content(val track: Track, val inFavourite: Boolean) : AudioplayerScreenState()
    object Error : AudioplayerScreenState()
}
