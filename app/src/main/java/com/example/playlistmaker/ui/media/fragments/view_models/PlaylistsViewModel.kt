package com.example.playlistmaker.ui.media.fragments.view_models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.entities.Playlist
import com.example.playlistmaker.domain.storage.use_cases.GetDataUseCase
import com.example.playlistmaker.ui.media.fragments.screen_states.MediaScreenState
import com.example.playlistmaker.utils.isEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PlaylistsViewModel(
    private val getPlaylistsUseCaseImpl: GetDataUseCase<List<Playlist>>
) : ViewModel() {

    private val _screenState: MutableLiveData<MediaScreenState> = MutableLiveData(MediaScreenState.Loading)
    val screenState: LiveData<MediaScreenState>
        get() = _screenState

    fun checkContent(list: List<Playlist>) {
        viewModelScope.launch(Dispatchers.IO) {
            getPlaylistsUseCaseImpl.get().collect { playlists ->
                if (!isEquals(list, playlists) || playlists.isEmpty()) {
                    if (playlists.isEmpty()) _screenState.postValue(MediaScreenState.Empty)
                    else _screenState.postValue(MediaScreenState.Content(playlists))
                }
            }
        }
    }

}