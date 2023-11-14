package com.example.playlistmaker.ui.playlist_edit.view_model

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.entities.Playlist
import com.example.playlistmaker.domain.storage.use_cases.GetItemByIdUseCase
import com.example.playlistmaker.domain.storage.use_cases.GetItemUseCase
import com.example.playlistmaker.domain.storage.use_cases.StoreItemUseCase
import com.example.playlistmaker.ui.playlist_creation.screen_state.PlaylistCreationScreenContent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.flow.singleOrNull
import kotlinx.coroutines.launch

class PlaylistEditViewModel(
    private val storePlaylistInDbUseCase: StoreItemUseCase<Playlist>,
    private val storePhotoUseCase: StoreItemUseCase<Uri>,
    private val getPhotoByIdUseCase: GetItemByIdUseCase<Uri>,
    private val getIdUseCase: GetItemUseCase<Long>,
    private val getPlaylistByIdUseCase: GetItemByIdUseCase<Playlist>,
    private val storeIdUseCase: StoreItemUseCase<Long>
) : ViewModel() {

    private val _screenState: MutableLiveData<PlaylistCreationScreenContent> =
        MutableLiveData(PlaylistCreationScreenContent("", "", 0L))
    val screenState: LiveData<PlaylistCreationScreenContent>
        get() = _screenState

    private var savedPlaylist: Playlist? = null

    init {
        viewModelScope.launch(Dispatchers.IO) {
            val id = getIdUseCase.get().single()
            getPlaylistByIdUseCase.get(id ?: 0L).single()?.run {
                savedPlaylist = this
                _screenState.postValue(
                    PlaylistCreationScreenContent(
                        playlistName = name,
                        playlistDescription = description,
                        playlistPhotoId = photoId
                    )
                )
            }
        }
    }

    fun saveState(name: String, description: String, photoId: Long) {
        _screenState.postValue(
            PlaylistCreationScreenContent(
                playlistName = name,
                playlistDescription = description,
                playlistPhotoId = photoId
            )
        )
    }

    fun storePhoto(uri: Uri) {
        storePhotoUseCase.store(uri)
        viewModelScope.launch(Dispatchers.IO) {
            _screenState.postValue(screenState.value?.copy(
                playlistPhotoId = getIdUseCase.get().singleOrNull() ?: 0L
            ))
        }
    }

    suspend fun getPhotoById(id: Long): Uri? = getPhotoByIdUseCase.get(id).single()

    fun storePlaylist(playlist: Playlist) {
        screenState.value?.run {
            if (savedPlaylist != null) {
                viewModelScope.launch(Dispatchers.IO) {
                    storeIdUseCase.store(savedPlaylist!!.id)
                    storePlaylistInDbUseCase.store(
                        playlist.copy(
                            id = savedPlaylist!!.id,
                            trackIdsList = savedPlaylist!!.trackIdsList
                        )
                    )
                }
            }
        }
    }
}