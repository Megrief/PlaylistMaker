package com.example.playlistmaker.data.settings

import android.content.Context
import android.content.SharedPreferences
import com.example.playlistmaker.app.App
import com.example.playlistmaker.domain.settings.SettingsRepository
import com.example.playlistmaker.domain.settings.entity.ThemeFlag
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class SettingsRepoImpl(
    private val sharedPrefs: SharedPreferences,
    private val context: Context,
    private val gson: Gson) : SettingsRepository {

    override fun switchTheme(checked: Boolean) {
        (context as App).switchTheme(checked)
    }

    override fun store(item: ThemeFlag?) {
        gson.toJson(item, ThemeFlag::class.java).also { themeFlagJson ->
            sharedPrefs.edit().putString(THEME, themeFlagJson).apply()
        }
    }

    override fun get(): Flow<ThemeFlag?> {
        return flow {
            sharedPrefs.getString(THEME, null)?.let { themeFlagJson ->
                emit(gson.fromJson(themeFlagJson, ThemeFlag::class.java))
            }
        }
    }

    companion object {
        private const val THEME = "THEME"
    }
}