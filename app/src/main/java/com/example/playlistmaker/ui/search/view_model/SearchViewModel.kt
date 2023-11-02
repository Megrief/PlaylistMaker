package com.example.playlistmaker.ui.search.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.entities.Track
import com.example.playlistmaker.domain.search.SearchUseCase
import com.example.playlistmaker.domain.storage.use_cases.GetDataUseCase
import com.example.playlistmaker.domain.storage.use_cases.StoreDataUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.singleOrNull
import kotlinx.coroutines.launch

class SearchViewModel(
    private val searchUseCase: SearchUseCase,
    private val storeTrackUseCase: StoreDataUseCase<Track>,
    private val storeTrackListUseCase: StoreDataUseCase<List<Track>>,
    private val getTrackListUseCase: GetDataUseCase<List<Track>>
) : ViewModel() {

    private val _searchScreenState = MutableLiveData<SearchScreeenState>(SearchScreeenState.Empty)
    val searchScreenState: LiveData<SearchScreeenState>
        get() = _searchScreenState

    private var savedValue = ""
    private var searchJob: Job? = null
        set(value) {
            field?.cancel()
            field = value
        }

    override fun onCleared() {
        super.onCleared()
        _searchScreenState.postValue(SearchScreeenState.Empty)
        searchJob = null
    }

    fun search(term: String) {
        if (savedValue != term) {
            if (term.isBlank()) {
                searchJob = null
            } else {
                savedValue = term
                setSearchJob(term)
            }
        }
    }

    fun clear() {
        _searchScreenState.postValue(SearchScreeenState.Empty)
    }

    fun showHistory() {
        viewModelScope.launch {
            getTrackListUseCase.get().collect { history ->
                if (history.isNotEmpty()) {
                    _searchScreenState.postValue(SearchScreeenState.SearchHistory(history))
                } else _searchScreenState.postValue(SearchScreeenState.Empty)
            }
        }
    }

    fun onTrackClick(track: Track) {
        viewModelScope.launch(Dispatchers.IO) {
            storeTrackUseCase.store(track)

            getTrackListUseCase.get().collect { history ->
                with(history.toMutableList()) {
                    remove(track)
                    add(0, track)
                    if (size > 10) removeLast()
                    if (_searchScreenState.value is SearchScreeenState.SearchHistory) {
                        _searchScreenState.postValue(SearchScreeenState.SearchHistory(this))
                    }
                    storeTrackListUseCase.store(this)
                }
            }
        }
    }

    fun clearHistory() {
        storeTrackListUseCase.store(emptyList())
        _searchScreenState.postValue(SearchScreeenState.Empty)
    }

    private fun setSearchJob(term: String) {
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(SEARCH_DEBOUNCE_DELAY_MILLIS)
            _searchScreenState.value = SearchScreeenState.IsLoading
            onResult(searchUseCase.search(term).singleOrNull())
        }
    }

    private fun onResult(result: List<Track>?) {
        when {
            result == null -> _searchScreenState.postValue(SearchScreeenState.NoInternetConnection)
            result.isEmpty() -> _searchScreenState.postValue(SearchScreeenState.NoResults)
            else -> _searchScreenState.postValue(SearchScreeenState.SearchSuccess(result))
        }
    }

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY_MILLIS = 2000L
    }
}