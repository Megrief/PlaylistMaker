package com.example.playlistmaker.domain.settings

import com.example.playlistmaker.data.settings.dto.ThemeCode
import com.example.playlistmaker.domain.storage.StorageManagerRepo

interface SettingsRepository : StorageManagerRepo<ThemeCode?> {

    fun switchTheme(checked: Boolean)
}