package com.example.playlistmaker.ui.settings

import android.content.Intent
import com.example.playlistmaker.data.settings.dto.ThemeCode

data class SettingsScreenState(
    val theme: ThemeCode,
    val shareApp: Intent,
    val mailToSupport: Intent,
    val userAgreement: Intent
)
