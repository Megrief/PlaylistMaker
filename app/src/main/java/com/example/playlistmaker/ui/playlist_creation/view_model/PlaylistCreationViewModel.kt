package com.example.playlistmaker.ui.playlist_creation.view_model

import android.net.Uri
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.domain.media.entity.Playlist
import com.example.playlistmaker.domain.media.playlists.use_cases_impl.StorePhotoUseCase
import com.example.playlistmaker.domain.storage.use_cases.StoreDataUseCase

class PlaylistCreationViewModel(
    private val storePlaylistInDb: StoreDataUseCase<Playlist>,
    private val storePhotoUseCase: StorePhotoUseCase,
) : ViewModel() {


    fun storePhoto(uri: Uri): String = storePhotoUseCase.store(uri)

    fun storePlaylist(playlist: Playlist) {
        storePlaylistInDb.store(playlist)
    }
}