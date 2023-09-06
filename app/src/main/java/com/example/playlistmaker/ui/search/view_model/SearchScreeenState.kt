package com.example.playlistmaker.ui.search.view_model

import com.example.playlistmaker.domain.entity.Track

sealed class SearchScreeenState {
    object Empty : SearchScreeenState()
    data class SearchHistory(val trackList: List<Track>) : SearchScreeenState()
    object IsLoading : SearchScreeenState()
    data class SearchSuccess(val trackList: List<Track>) : SearchScreeenState()
    object NoInternetConnection : SearchScreeenState()
    object NoResults : SearchScreeenState()
}
