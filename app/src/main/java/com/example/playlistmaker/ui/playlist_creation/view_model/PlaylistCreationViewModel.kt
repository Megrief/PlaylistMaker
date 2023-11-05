package com.example.playlistmaker.ui.playlist_creation.view_model

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.domain.entities.Playlist
import com.example.playlistmaker.domain.media.playlists.use_cases_impl.StorePhotoUseCase
import com.example.playlistmaker.domain.storage.use_cases.StoreDataUseCase
import com.example.playlistmaker.ui.playlist_creation.screen_state.PlaylistCreationScreenState
import java.io.File

class PlaylistCreationViewModel(
    private val storePlaylistInDb: StoreDataUseCase<Playlist>,
    private val storePhotoUseCase: StorePhotoUseCase,
) : ViewModel() {

    private val _screenState: MutableLiveData<PlaylistCreationScreenState> = MutableLiveData(PlaylistCreationScreenState("", "", null, false))

    val screenState: LiveData<PlaylistCreationScreenState>
        get() = _screenState

    fun saveState(name: String, description: String, notEmpty: Boolean) {
        _screenState.value = screenState.value?.copy(playlistName = name, playlistDescription = description, notEmpty = notEmpty)
    }

    fun storePhoto(uri: Uri): File {
        return storePhotoUseCase.store(uri).also {
            _screenState.postValue(screenState.value?.copy(playlistPhoto = it))
        }
    }

    fun storePlaylist() {
        screenState.value?.run {
            val playlist = Playlist(
                name = playlistName,
                description = playlistDescription,
                photoFile = playlistPhoto
            )
            storePlaylistInDb.store(playlist)
        }
    }
}