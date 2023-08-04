package com.example.playlistmaker.ui.search.vm

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
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.domain.entities.Track
import com.example.playlistmaker.domain.interactors.SearchInteractor
import com.example.playlistmaker.domain.interactors.StorageInteractor

class SearchViewModel(
    application: Application,
    private val searchInteractor: SearchInteractor,
    private val storageInteractorTrack: StorageInteractor<Track?>,
    private val storageInteractorList: StorageInteractor<List<Track>>
) : AndroidViewModel(application) {

    private val searchScreenStateLiveData = MutableLiveData<SearchScreeenState>(SearchScreeenState.Empty)

    private var savedValue = ""
    private val getResults = Runnable {
        searchScreenStateLiveData.value = SearchScreeenState.IsLoading
        searchInteractor.search(savedValue) {
            when {
                it == null -> searchScreenStateLiveData.postValue(SearchScreeenState.NoInternetConnection)
                it.isEmpty() -> searchScreenStateLiveData.postValue(SearchScreeenState.NoResults)
                else -> searchScreenStateLiveData.postValue(SearchScreeenState.SearchSuccess(it))
            }
        }
    }
    private val mainHandler = Handler(Looper.getMainLooper())
    private val searchRequestDebouncer = SearchRequestDebouncer(mainHandler)
    private var history: List<Track> = emptyList()

    init {
        storageInteractorList.get(HISTORY_KEY) {
            history = it
        }
    }

    fun getSearchScreenStateLiveData(): LiveData<SearchScreeenState> = searchScreenStateLiveData

    fun search(term: String) {
        if (savedValue != term || searchScreenStateLiveData.value !is SearchScreeenState.SearchSuccess) {
            savedValue = term
            searchRequestDebouncer.searchDebounce(getResults)
        }
    }

    fun clear() {
        searchScreenStateLiveData.postValue(SearchScreeenState.Empty)
        mainHandler.removeCallbacks(getResults)
    }

    // History new

    fun showHistory() {
        if (history.isNotEmpty()) {
            searchScreenStateLiveData.postValue(SearchScreeenState.SearchHistory(history))
        } else searchScreenStateLiveData.postValue(SearchScreeenState.Empty)
    }

    fun addToHistory(track: Track) {
        storageInteractorTrack.save(TRACK, track)
        storageInteractorList.get(HISTORY_KEY) {
            with(it.toMutableList()) {
                remove(track)
                add(0, track)
                if (size > 10) removeLast()
                history = this
                if (searchScreenStateLiveData.value is SearchScreeenState.SearchHistory) {
                    searchScreenStateLiveData.postValue(SearchScreeenState.SearchHistory(history))
                }
                storageInteractorList.save(HISTORY_KEY, this)
            }
        }
    }

    fun clearHistory() {
        storageInteractorList.save(HISTORY_KEY, emptyList())
        searchScreenStateLiveData.postValue(SearchScreeenState.Empty)
    }
    ////

    companion object {
        const val HISTORY_KEY = "HISTORY_KEY"
        const val TRACK = "TRACK"

        fun getSearchViewModelFactory() : ViewModelProvider.Factory {
            return viewModelFactory {
                initializer {
                    val application = this[APPLICATION_KEY] as App
                    val context = application.applicationContext
                    val searchInteractor = Creator.createSearchInteractor()
                    val storageInteractorTrack = Creator.createStorageInteractorTrack(context)
                    val storageInteractorList = Creator.createStorageInteractorList(context)
                    SearchViewModel(
                        application,
                        searchInteractor,
                        storageInteractorTrack,
                        storageInteractorList
                    )
                }
            }
        }
    }
}