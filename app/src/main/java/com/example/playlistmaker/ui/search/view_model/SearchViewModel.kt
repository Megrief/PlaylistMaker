package com.example.playlistmaker.ui.search.view_model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.entity.Track
import com.example.playlistmaker.domain.storage.use_cases.GetDataUseCase
import com.example.playlistmaker.domain.storage.use_cases.StoreDataUseCase
import com.example.playlistmaker.utils.debounce

class SearchViewModel(
    private val searchUseCase: GetDataUseCase<List<Track>?>,
    private val storeTrackUseCase: StoreDataUseCase<Track>,
    private val storeTrackListUseCase: StoreDataUseCase<List<Track>>,
    private val getTrackListUseCase: GetDataUseCase<List<Track>>
) : ViewModel() {

    private val _searchScreenState = MutableLiveData<SearchScreeenState>(SearchScreeenState.Empty)
    val searchScreenState: LiveData<SearchScreeenState>
        get() = _searchScreenState

    private var savedValue = ""

    private val searchDebounce = debounce<String>(
        SEARCH_DEBOUNCE_DELAY_MILLIS,
        viewModelScope,
        true
    ) {
            Log.e("Debug", "In search debounce")
            _searchScreenState.value = SearchScreeenState.IsLoading
            searchUseCase.get(savedValue) { results ->
                when {
                    results == null -> _searchScreenState.postValue(SearchScreeenState.NoInternetConnection)
                    results.isEmpty() -> _searchScreenState.postValue(SearchScreeenState.NoResults)
                    else -> _searchScreenState.postValue(SearchScreeenState.SearchSuccess(results))
                }
            }
        }

    fun search(term: String) {
        if (savedValue != term || _searchScreenState.value !is SearchScreeenState.SearchSuccess) {
            savedValue = term
            searchDebounce(savedValue) // new
        }
    }

    fun clear() {
        _searchScreenState.postValue(SearchScreeenState.Empty)
    }

    fun showHistory() {
        getTrackListUseCase.get(HISTORY_KEY) { history ->
            if (history.isNotEmpty()) {
                _searchScreenState.postValue(SearchScreeenState.SearchHistory(history))
            } else _searchScreenState.postValue(SearchScreeenState.Empty)
        }
    }

    fun addToHistory(track: Track) {
        storeTrackUseCase.store(TRACK, track)
        getTrackListUseCase.get(HISTORY_KEY) { history ->
            with(history.toMutableList()) {
                remove(track)
                add(0, track)
                if (size > 10) removeLast()
                if (_searchScreenState.value is SearchScreeenState.SearchHistory) {
                    _searchScreenState.postValue(SearchScreeenState.SearchHistory(this))
                }
                storeTrackListUseCase.store(HISTORY_KEY, this)
            }
        }
    }

    fun clearHistory() {
        storeTrackListUseCase.store(HISTORY_KEY, emptyList())
        _searchScreenState.postValue(SearchScreeenState.Empty)
    }

    companion object {
        const val HISTORY_KEY = "HISTORY_KEY"
        const val TRACK = "TRACK"
        private const val SEARCH_DEBOUNCE_DELAY_MILLIS = 3000L

    }
}