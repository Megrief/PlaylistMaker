package com.example.playlistmaker.ui.search.view_model

import android.app.Application
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.playlistmaker.app.App
import com.example.playlistmaker.domain.entities.Track
import com.example.playlistmaker.domain.use_cases.GetDataUseCase
import com.example.playlistmaker.domain.use_cases.StoreDataUseCase
import com.example.playlistmaker.utils.creator.Creator

class SearchViewModel(
    application: Application,
    private val searchUseCase: GetDataUseCase<List<Track>?>,
    private val storeTrackUseCase: StoreDataUseCase<Track>,
    private val storeTrackListUseCase: StoreDataUseCase<List<Track>>,
    private val getTrackListUseCase: GetDataUseCase<List<Track>>
) : AndroidViewModel(application) {

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

        fun getSearchViewModelFactory() : ViewModelProvider.Factory {
            return viewModelFactory {
                initializer {
                    val application = this[APPLICATION_KEY] as App
                    val context = application.applicationContext
                    val searchInteractor = Creator.createSearchUseCase()
                    val storeTrackUseCase = Creator.createStoreTrackUseCase(context)
                    val storeTrackListUseCase = Creator.createStoreTrackListUseCase(context)
                    val getTrackListUseCase = Creator.createGetTrackListUseCase(context)
                    SearchViewModel(
                        application,
                        searchInteractor,
                        storeTrackUseCase,
                        storeTrackListUseCase,
                        getTrackListUseCase
                    )
                }
            }
        }
    }
}