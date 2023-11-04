package com.example.playlistmaker.ui.media.fragments.view_models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.entities.Track
import com.example.playlistmaker.domain.storage.use_cases.GetDataUseCase
import com.example.playlistmaker.domain.storage.use_cases.StoreDataUseCase
import com.example.playlistmaker.ui.media.fragments.screen_states.MediaScreenState
import com.example.playlistmaker.utils.isEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FavoritesViewModel(
    private val getFavouritesUseCase: GetDataUseCase<List<Track>>,
    private val storeTrackUseCase: StoreDataUseCase<Track>
) : ViewModel() {

    private val _screenState = MutableLiveData<MediaScreenState>(MediaScreenState.Loading)
    val screenState: LiveData<MediaScreenState>
        get() = _screenState

    fun checkContent(list: List<Track>) {
        viewModelScope.launch(Dispatchers.IO) {
            getFavouritesUseCase.get().collect { favorites ->
                if (!isEquals(list, favorites) || favorites.isEmpty()) {
                    if (favorites.isEmpty()) _screenState.postValue(MediaScreenState.Empty)
                    else _screenState.postValue(MediaScreenState.Content(favorites))
                }
            }
        }
    }

    fun onTrackClick(track: Track) {
        storeTrackUseCase.store(track)
    }

}