package com.example.playlistmaker.data.settings

import android.content.Intent
import android.content.SharedPreferences
import com.example.playlistmaker.app.App
import com.example.playlistmaker.domain.settings.SettingsRepository
import com.example.playlistmaker.domain.settings.entity.ThemeFlag
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class SettingsImpl(
    private val sharedPrefs: SharedPreferences,
    private val sendBroadcast: (Intent) -> Unit,
    private val gson: Gson) : SettingsRepository {

    override fun switchTheme(checked: Boolean) {
        val intent = Intent(App.ACTION_SWITCH_THEME).apply {
            putExtra(App.THEME_FLAG, checked)
        }

        sendBroadcast(intent)
    }

    override fun store(item: ThemeFlag?): Boolean =
        gson.toJson(item, ThemeFlag::class.java).let { themeFlagJson ->
            sharedPrefs.edit().putString(THEME, themeFlagJson).commit()
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