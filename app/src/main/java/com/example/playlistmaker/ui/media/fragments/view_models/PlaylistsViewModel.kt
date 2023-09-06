package com.example.playlistmaker.ui.media.fragments.view_models

import androidx.lifecycle.ViewModel
import com.example.playlistmaker.domain.media.entity.Playlist
import com.example.playlistmaker.domain.storage.use_cases.GetDataUseCase
import com.example.playlistmaker.domain.storage.use_cases.StoreDataUseCase

class PlaylistsViewModel(
    private val getPlaylists: GetDataUseCase<List<Playlist>>,
    private val storePlaylists: StoreDataUseCase<List<Playlist>>,
    private val storePlaylist: StoreDataUseCase<Playlist>,
) : ViewModel() {


}