package com.example.playlistmaker.data.settings

import android.content.Context
import com.example.playlistmaker.app.App
import com.example.playlistmaker.data.settings.dto.ThemeCode
import com.example.playlistmaker.domain.settings.SettingsRepository
import com.google.gson.Gson

class SettingsRepoImpl(private val context: Context) : SettingsRepository {
    private val sharedPrefs = context.getSharedPreferences(App.PLAYLIST_MAKER, Context.MODE_PRIVATE)
    private val gson = Gson()

    override fun switchTheme(checked: Boolean) {
        context as App
        context.switchTheme(checked)
    }

    override fun store(key: String, item: ThemeCode?) {
        gson.toJson(item, ThemeCode::class.java).let {
            sharedPrefs.edit().putString(key, it).apply()
        }
    }

    override fun get(key: String): ThemeCode? {
        return sharedPrefs.getString(key, null)?.let {
            gson.fromJson(it, ThemeCode::class.java)
        }
    }
}