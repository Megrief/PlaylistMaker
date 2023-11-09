package com.example.playlistmaker.ui.playlist_creation.view_model

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.entities.Playlist
import com.example.playlistmaker.domain.storage.use_cases.GetItemByIdUseCase
import com.example.playlistmaker.domain.storage.use_cases.GetItemUseCase
import com.example.playlistmaker.domain.storage.use_cases.StoreItemUseCase
import com.example.playlistmaker.ui.playlist_creation.screen_state.PlaylistCreationScreenState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.flow.singleOrNull
import kotlinx.coroutines.launch

class PlaylistCreationViewModel(
    private val storePlaylistInDb: StoreItemUseCase<Playlist>,
    private val storePhotoUseCaseImpl: StoreItemUseCase<Uri>,
    private val getPhotoByIdUseCase: GetItemByIdUseCase<Uri>,
    private val getPhotoIdUseCase: GetItemUseCase<Long>
) : ViewModel() {

    private val _screenState: MutableLiveData<PlaylistCreationScreenState> =
        MutableLiveData(PlaylistCreationScreenState("", "", 0L))

    val screenState: LiveData<PlaylistCreationScreenState>
        get() = _screenState

    fun saveState(name: String, description: String, photoId: Long) {
        _screenState.postValue(
            PlaylistCreationScreenState(
                playlistName = name,
                playlistDescription = description,
                playlistPhotoId = photoId
            )
        )
    }

    fun storePhoto(uri: Uri) {
        storePhotoUseCaseImpl.store(uri)
        viewModelScope.launch(Dispatchers.IO) {
            _screenState.postValue(screenState.value?.copy(
                playlistPhotoId = getPhotoIdUseCase.get().singleOrNull() ?: 0L
            ))
        }
    }

    suspend fun getPhotoById(id: Long): Uri? = getPhotoByIdUseCase.get(id).single()

    fun storePlaylist(playlist: Playlist) {
        screenState.value?.run {
            storePlaylistInDb.store(playlist)
        }
    }
}