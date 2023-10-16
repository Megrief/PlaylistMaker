package com.example.playlistmaker.ui.media.fragments.screen_states

import com.example.playlistmaker.domain.entity.Track

sealed class FavouritesScreenState {
    object Loading : FavouritesScreenState()
    class Content(val content: List<Track>) : FavouritesScreenState()
}
