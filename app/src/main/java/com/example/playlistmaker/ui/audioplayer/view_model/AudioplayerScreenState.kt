package com.example.playlistmaker.ui.audioplayer.view_model

import com.example.playlistmaker.domain.entity.Track

sealed class AudioplayerScreenState {
    object Loading : AudioplayerScreenState()
    class Content(val track: Track) : AudioplayerScreenState()
    object Error : AudioplayerScreenState()
}
