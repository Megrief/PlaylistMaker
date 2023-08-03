package com.example.playlistmaker.ui.utils

import android.os.Handler
import android.os.Looper


class ItemClickDebouncer {
    private var isClickAllowed = true
    private val mainHandler = Handler(Looper.getMainLooper())

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

    companion object {
        const val CLICK_DEBOUNCE_DELAY = 1000L
    }
}