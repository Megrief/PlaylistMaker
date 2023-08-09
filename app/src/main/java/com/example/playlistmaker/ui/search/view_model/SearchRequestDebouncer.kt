package com.example.playlistmaker.ui.search.view_model

import android.os.Handler

class SearchRequestDebouncer(private val mainHandler: Handler) {
    fun searchDebounce(request: Runnable) {
        mainHandler.removeCallbacks(request)
        mainHandler.postDelayed(request, SEARCH_DEBOUNCE_DELAY_MILLIS)
    }

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY_MILLIS = 3000L
    }
}