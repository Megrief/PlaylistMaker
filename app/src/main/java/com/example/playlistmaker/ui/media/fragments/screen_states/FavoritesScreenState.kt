package com.example.playlistmaker.ui.media.fragments.screen_states

import com.example.playlistmaker.domain.entity.Track

sealed class FavoritesScreenState {
    object Loading : FavoritesScreenState()
    class Content(val content: List<Track>) : FavoritesScreenState()
}
