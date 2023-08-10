package com.example.playlistmaker.app

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.example.playlistmaker.data.settings.dto.ThemeCode
import com.example.playlistmaker.utils.creator.Creator

class App : Application() {
    private val getThemeCodeUseCase by lazy { Creator.createGetThemeCodeUseCase(this) }
    private val storeThemeCodeUseCase by lazy { Creator.createStoreThemeCodeUseCase(this) }

    override fun onCreate() {
        super.onCreate()
        getThemeCodeUseCase.get(THEME) {
            AppCompatDelegate.setDefaultNightMode(
                if (it != null) {
                    if (it.code) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
                } else AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
            )
        }
    }

    fun switchTheme(darkThemeEnabled: Boolean) {
        storeThemeCodeUseCase.store(THEME, ThemeCode(darkThemeEnabled))
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }

    companion object {
        const val PLAYLIST_MAKER = "PLAYLIST_MAKER"
        const val THEME = "THEME"
    }
}