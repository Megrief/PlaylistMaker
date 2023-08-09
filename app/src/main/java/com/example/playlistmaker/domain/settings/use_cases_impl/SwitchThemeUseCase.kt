package com.example.playlistmaker.domain.settings.use_cases_impl

import android.util.Log
import com.example.playlistmaker.domain.settings.SettingsRepository

class SwitchThemeUseCase(private val repository: SettingsRepository) {

    fun switchTheme(checked: Boolean) {
        Log.wtf("SWITCH", "In useCase switched")

        repository.switchTheme(checked)
    }
}