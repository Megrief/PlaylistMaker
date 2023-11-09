package com.example.playlistmaker.domain.settings

import com.example.playlistmaker.domain.settings.entity.ThemeFlag
import com.example.playlistmaker.domain.storage.repos.StorageManagerRepo

interface SettingsRepository : StorageManagerRepo<ThemeFlag?> {

    fun switchTheme(checked: Boolean)
}