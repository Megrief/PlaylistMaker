package com.example.playlistmaker.domain.settings

import com.example.playlistmaker.domain.settings.entity.ThemeFlag
import com.example.playlistmaker.domain.storage.repos.StorageManager

interface SettingsRepository : StorageManager<ThemeFlag?> {

    fun switchTheme(checked: Boolean)
}