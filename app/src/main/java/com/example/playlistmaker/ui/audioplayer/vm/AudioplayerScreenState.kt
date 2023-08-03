package com.example.playlistmaker.ui.audioplayer.vm

import com.example.playlistmaker.domain.entities.Track

sealed class AudioplayerScreenState {
    object Loading : AudioplayerScreenState()
    class Content(val track: Track) : AudioplayerScreenState()
    object Error : AudioplayerScreenState()
}
