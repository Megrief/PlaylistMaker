package com.example.playlistmaker.ui.media.fragments.view_models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.entity.Track
import com.example.playlistmaker.domain.storage.use_cases.GetDataUseCase
import com.example.playlistmaker.domain.storage.use_cases.StoreDataUseCase
import com.example.playlistmaker.ui.media.fragments.screen_states.FavouritesScreenState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FavouritesViewModel(
    private val getFavouritesUseCase: GetDataUseCase<List<Track>>,
    private val storeTrackUseCase: StoreDataUseCase<Track>
) : ViewModel() {

    private val _screenState = MutableLiveData<FavouritesScreenState>(FavouritesScreenState.Loading)
    val screenState: LiveData<FavouritesScreenState>
        get() = _screenState

    fun checkContent(list: List<Track>) {
        viewModelScope.launch(Dispatchers.IO) {
            getFavouritesUseCase.get().collect { favourites ->
                if (!isEquals(list, favourites) || favourites.isEmpty()) {
                    _screenState.postValue(FavouritesScreenState.Content(favourites))
                }
            }
        }
    }

    fun onTrackClick(track: Track) {
        storeTrackUseCase.store(track)
    }

    private fun isEquals(oldList: List<Track>, newList: List<Track>): Boolean {
        return if (oldList.size != newList.size) {
            false
        } else {
            for (index in oldList.indices) {
                if (oldList[index].trackId != newList[index].trackId) return false
            }
            true
        }
    }
}