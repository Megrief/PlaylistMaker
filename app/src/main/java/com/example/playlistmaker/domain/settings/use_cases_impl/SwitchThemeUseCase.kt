package com.example.playlistmaker.domain.settings.use_cases_impl

import com.example.playlistmaker.domain.settings.SettingsRepository

class SwitchThemeUseCase(private val repository: SettingsRepository) {

    fun switchTheme(checked: Boolean) {

        repository.switchTheme(checked)
    }
}