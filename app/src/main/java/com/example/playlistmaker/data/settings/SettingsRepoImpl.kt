package com.example.playlistmaker.data.settings

import android.content.Context
import com.example.playlistmaker.app.App
import com.example.playlistmaker.domain.settings.entity.ThemeFlag
import com.example.playlistmaker.domain.settings.SettingsRepository
import com.google.gson.Gson

class SettingsRepoImpl(private val context: Context, private val gson: Gson) : SettingsRepository {
    private val sharedPrefs = context.getSharedPreferences(App.PLAYLIST_MAKER, Context.MODE_PRIVATE)

    override fun switchTheme(checked: Boolean) {
        context as App
        context.switchTheme(checked)
    }

    override fun store(key: String, item: ThemeFlag?) {
        gson.toJson(item, ThemeFlag::class.java).also { themeFlagJson ->
            sharedPrefs.edit().putString(key, themeFlagJson).apply()
        }
    }

    override fun get(key: String): ThemeFlag? {
        return sharedPrefs.getString(key, null)?.let { themeFlagJson ->
            gson.fromJson(themeFlagJson, ThemeFlag::class.java)
        }
    }
}