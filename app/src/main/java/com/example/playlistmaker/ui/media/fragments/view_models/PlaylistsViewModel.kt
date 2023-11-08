package com.example.playlistmaker.ui.media.fragments.view_models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.entities.Playlist
import com.example.playlistmaker.domain.storage.use_cases.GetItemUseCase
import com.example.playlistmaker.ui.media.fragments.screen_states.MediaScreenState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PlaylistsViewModel(
    private val getPlaylistsUseCaseImpl: GetItemUseCase<List<Playlist>>
) : ViewModel() {

    private val _screenState: MutableLiveData<MediaScreenState> = MutableLiveData(MediaScreenState.Loading)
    val screenState: LiveData<MediaScreenState>
        get() = _screenState

    fun checkContent(list: List<Playlist>) {
        viewModelScope.launch(Dispatchers.IO) {
            getPlaylistsUseCaseImpl.get().collect { playlists ->
                when {
                    playlists == null -> { }
                    playlists.isEmpty() -> _screenState.postValue(MediaScreenState.Empty)
                    list.size != playlists.size -> _screenState.postValue(MediaScreenState.Content(playlists))
                    else -> {
                        for (ind in playlists.indices) {
                            if (playlists[ind].trackIdsList.size != list[ind].trackIdsList.size
                                    || playlists[ind].id != list[ind].id) {
                                _screenState.postValue(MediaScreenState.Content(playlists))
                                break
                            }
                        }
                    }
                }
            }
        }
    }

}