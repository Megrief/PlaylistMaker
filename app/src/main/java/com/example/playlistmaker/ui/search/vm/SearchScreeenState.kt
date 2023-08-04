package com.example.playlistmaker.ui.search.vm

import com.example.playlistmaker.domain.entities.Track

sealed class SearchScreeenState {
    object Empty : SearchScreeenState()
    data class SearchHistory(val trackList: List<Track>) : SearchScreeenState()
    object IsLoading : SearchScreeenState()
    data class SearchSuccess(val trackList: List<Track>) : SearchScreeenState()
    object NoInternetConnection : SearchScreeenState()
    object NoResults : SearchScreeenState()
}
