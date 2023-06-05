package com.example.playlistmaker

import android.app.Application
import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import com.example.playlistmaker.utils.Constants

class App : Application() {
    private val sharedPreferences by lazy { this.getSharedPreferences(Constants.PLAYLIST_MAKER.key, Context.MODE_PRIVATE) }
    val themeCode by lazy {
        sharedPreferences.getInt(Constants.DARK_THEME.key, -1).let {
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
                sharedPreferences.edit().putInt(Constants.DARK_THEME.key, AppCompatDelegate.MODE_NIGHT_YES).apply()
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                sharedPreferences.edit().putInt(Constants.DARK_THEME.key, AppCompatDelegate.MODE_NIGHT_NO).apply()
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }
}