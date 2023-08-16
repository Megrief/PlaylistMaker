package com.example.playlistmaker.app

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.example.playlistmaker.domain.settings.entity.ThemeFlag
import com.example.playlistmaker.utils.creator.Creator

class App : Application() {
    private val getThemeFlagUseCase by lazy { Creator.createGetThemeFlagUseCase(this) }
    private val storeThemeFlagUseCase by lazy { Creator.createStoreThemeFlagUseCase(this) }

    override fun onCreate() {
        super.onCreate()
        getThemeFlagUseCase.get(THEME) {
            AppCompatDelegate.setDefaultNightMode(
                if (it != null) {
                    if (it.flag) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
                } else AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
            )
        }
    }

    fun switchTheme(darkThemeEnabled: Boolean) {
        storeThemeFlagUseCase.store(THEME, ThemeFlag(darkThemeEnabled))
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