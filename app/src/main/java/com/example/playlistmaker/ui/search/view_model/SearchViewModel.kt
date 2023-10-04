package com.example.playlistmaker.ui.search.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.entity.Track
import com.example.playlistmaker.domain.storage.use_cases.GetDataUseCase
import com.example.playlistmaker.domain.storage.use_cases.StoreDataUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.singleOrNull
import kotlinx.coroutines.launch

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
    private var searchJob: Job? = null

    fun search(term: String) {
        if (savedValue != term) {
            if (term.isBlank()) {
                searchJob?.cancel()
                searchJob = null
            } else {
                savedValue = term
                setSearchJob(term)
            }
        }
    }

    private fun setSearchJob(term: String) {
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(SEARCH_DEBOUNCE_DELAY_MILLIS)
            _searchScreenState.value = SearchScreeenState.IsLoading
            onResult(searchUseCase.get(term).singleOrNull())
        }
    }

    private fun onResult(result: List<Track>?) {
        when {
            result == null -> _searchScreenState.postValue(SearchScreeenState.NoInternetConnection)
            result.isEmpty() -> _searchScreenState.postValue(SearchScreeenState.NoResults)
            else -> _searchScreenState.postValue(SearchScreeenState.SearchSuccess(result))
        }
    }

    fun clear() {
        _searchScreenState.postValue(SearchScreeenState.Empty)
    }

    fun showHistory() {
        viewModelScope.launch {
            getTrackListUseCase.get(HISTORY_KEY).collect { history ->
                if (history.isNotEmpty()) {
                    _searchScreenState.postValue(SearchScreeenState.SearchHistory(history))
                } else _searchScreenState.postValue(SearchScreeenState.Empty)
            }
        }
    }

    fun addToHistory(track: Track) {
        viewModelScope.launch(Dispatchers.IO) {
            storeTrackUseCase.store(TRACK, track)

            getTrackListUseCase.get(HISTORY_KEY).collect { history ->
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
    }

    fun clearHistory() {
        storeTrackListUseCase.store(HISTORY_KEY, emptyList())
        _searchScreenState.postValue(SearchScreeenState.Empty)
    }

    companion object {
        const val HISTORY_KEY = "HISTORY_KEY"
        const val TRACK = "TRACK"
        private const val SEARCH_DEBOUNCE_DELAY_MILLIS = 2000L

    }
}