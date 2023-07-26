package com.example.playlistmaker.presentaion.utils

import android.os.Handler
import android.os.Looper

class SearchRequestDebouncer {
    private val mainHandler = Handler(Looper.getMainLooper())

    fun searchDebounce(request: Runnable) {
        mainHandler.removeCallbacks(request)
        mainHandler.postDelayed(request, SEARCH_DEBOUNCE_DELAY)
    }
    companion object {
        const val SEARCH_DEBOUNCE_DELAY = 3000L
    }
}