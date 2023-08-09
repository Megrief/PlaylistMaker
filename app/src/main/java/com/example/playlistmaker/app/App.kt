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
                when (it?.code) {
                    ThemeCode.DAY_MODE_CODE -> AppCompatDelegate.MODE_NIGHT_NO
                    ThemeCode.NIGHT_MODE_CODE -> AppCompatDelegate.MODE_NIGHT_YES
                    else -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
                }
            )
        }
    }

    fun switchTheme(darkThemeEnabled: Boolean) {
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                storeThemeCodeUseCase.store(THEME, ThemeCode(ThemeCode.NIGHT_MODE_CODE))
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                storeThemeCodeUseCase.store(THEME, ThemeCode(ThemeCode.DAY_MODE_CODE))
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }

    companion object {
        const val PLAYLIST_MAKER = "PLAYLIST_MAKER"
        const val THEME = "THEME"
    }
}