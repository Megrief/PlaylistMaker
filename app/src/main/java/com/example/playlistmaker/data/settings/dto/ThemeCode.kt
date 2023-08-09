package com.example.playlistmaker.data.settings.dto

data class ThemeCode(
    val code: Int
) {
    companion object {
        const val NIGHT_MODE_CODE = 2
        const val DAY_MODE_CODE = 1
        const val SYSTEM_MODE_CODE = -1
    }
}