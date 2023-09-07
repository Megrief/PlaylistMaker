package com.example.playlistmaker.ui.search.view_model

import android.os.Handler
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.domain.entity.Track
import com.example.playlistmaker.domain.storage.use_cases.GetDataUseCase
import com.example.playlistmaker.domain.storage.use_cases.StoreDataUseCase
import com.example.playlistmaker.ui.search.view_model.util.SearchRequestDebouncer
import org.koin.java.KoinJavaComponent.getKoin

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
        searchUseCase.get(savedValue) { results ->
            when {
                results == null -> searchScreenStateLiveData.postValue(SearchScreeenState.NoInternetConnection)
                results.isEmpty() -> searchScreenStateLiveData.postValue(SearchScreeenState.NoResults)
                else -> searchScreenStateLiveData.postValue(SearchScreeenState.SearchSuccess(results))
            }
        }
    }
    private val mainHandler: Handler = getKoin().get()
    private val searchRequestDebouncer: SearchRequestDebouncer = getKoin().get()

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
        getTrackListUseCase.get(HISTORY_KEY) { history ->
            with(history.toMutableList()) {
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