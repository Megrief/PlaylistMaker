package com.example.playlistmaker.utils

import android.os.Handler
import android.os.Looper


object ItemClickDebouncer {
    private var isClickAllowed = true
    private val mainHandler = Handler(Looper.getMainLooper())
    private const val CLICK_DEBOUNCE_DELAY = 1000L
    fun clickDebounce(): Boolean {
        return isClickAllowed.also {
            if (it) {
                isClickAllowed = false
                mainHandler.postDelayed(
                    { isClickAllowed = true },
                    CLICK_DEBOUNCE_DELAY
                )
            }
        }
    }
}