package com.example.playlistmaker.app

import android.app.Application
import android.content.Context
import androidx.appcompat.app.AppCompatDelegate

class App : Application() {
    private val sharedPreferences by lazy { this.getSharedPreferences(PLAYLIST_MAKER, Context.MODE_PRIVATE) }
    val themeCode by lazy {
        sharedPreferences.getInt(DARK_THEME, -1).let {
            when (it) {
                1 -> AppCompatDelegate.MODE_NIGHT_NO
                2 -> AppCompatDelegate.MODE_NIGHT_YES
                else -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
            }
        }
    }

    override fun onCreate() {
        super.onCreate()
        AppCompatDelegate.setDefaultNightMode(themeCode)
    }

    fun switchTheme(darkThemeEnabled: Boolean) {
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                sharedPreferences.edit().putInt(DARK_THEME, AppCompatDelegate.MODE_NIGHT_YES).apply()
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                sharedPreferences.edit().putInt(DARK_THEME, AppCompatDelegate.MODE_NIGHT_NO).apply()
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }

    companion object {
        const val PLAYLIST_MAKER = "PLAYLIST_MAKER"
        const val DARK_THEME = "DARK_THEME"
    }
}