package com.example.playlistmaker.ui.media.fragments.screen_states

import com.example.playlistmaker.domain.entities.EntityRoot

sealed class MediaScreenState {
    object Loading : MediaScreenState()
    object Empty : MediaScreenState()
    class Content(val content: List<EntityRoot>) : MediaScreenState()
}
