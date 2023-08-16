package com.example.playlistmaker.ui.search.view_model

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.domain.entities.Track
import com.example.playlistmaker.domain.storage.use_cases.GetDataUseCase
import com.example.playlistmaker.domain.storage.use_cases.StoreDataUseCase

class SearchViewModel(
    private val searchUseCase: GetDataUseCase<List<Track>?>,
    private val storeTrackUseCase: StoreDataUseCase<Track>,
    private val storeTrackListUseCase: StoreDataUseCase<List<Track>>,
    private val getTrackListUseCase: GetDataUseCase<List<Track>>
) : ViewModel() {

    private val searchScreenStateLiveData = MutableLiveData<SearchScreeenState>(SearchScreeenState.Empty)

    private var savedValue = ""
    private val getResults = Runnable {
        searchScreenStateLiveData.value = SearchScreeenState.IsLoading
        searchUseCase.get(savedValue) {
            when {
                it == null -> searchScreenStateLiveData.postValue(SearchScreeenState.NoInternetConnection)
                it.isEmpty() -> searchScreenStateLiveData.postValue(SearchScreeenState.NoResults)
                else -> searchScreenStateLiveData.postValue(SearchScreeenState.SearchSuccess(it))
            }
        }
    }
    private val mainHandler = Handler(Looper.getMainLooper())
    private val searchRequestDebouncer = SearchRequestDebouncer(mainHandler)

    fun getSearchScreenStateLiveData(): LiveData<SearchScreeenState> = searchScreenStateLiveData

    fun search(term: String) {
        if (savedValue != term || searchScreenStateLiveData.value !is SearchScreeenState.SearchSuccess) {
            savedValue = term
            searchRequestDebouncer.searchDebounce(getResults)
        }
    }

    fun clear() {
        searchScreenStateLiveData.postValue(SearchScreeenState.Empty)
        removeCallbacks()
    }

    fun removeCallbacks() {
        mainHandler.removeCallbacks(getResults)
    }

    fun showHistory() {
        getTrackListUseCase.get(HISTORY_KEY) { history ->
            if (history.isNotEmpty()) {
                searchScreenStateLiveData.postValue(SearchScreeenState.SearchHistory(history))
            } else searchScreenStateLiveData.postValue(SearchScreeenState.Empty)
        }
    }

    fun addToHistory(track: Track) {
        storeTrackUseCase.store(TRACK, track)
        getTrackListUseCase.get(HISTORY_KEY) {
            with(it.toMutableList()) {
                remove(track)
                add(0, track)
                if (size > 10) removeLast()
                if (searchScreenStateLiveData.value is SearchScreeenState.SearchHistory) {
                    searchScreenStateLiveData.postValue(SearchScreeenState.SearchHistory(this))
                }
                storeTrackListUseCase.store(HISTORY_KEY, this)
            }
        }
    }

    fun clearHistory() {
        storeTrackListUseCase.store(HISTORY_KEY, emptyList())
        searchScreenStateLiveData.postValue(SearchScreeenState.Empty)
    }

    companion object {
        const val HISTORY_KEY = "HISTORY_KEY"
        const val TRACK = "TRACK"

    }
}