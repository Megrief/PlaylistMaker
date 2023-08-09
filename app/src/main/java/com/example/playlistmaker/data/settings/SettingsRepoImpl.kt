package com.example.playlistmaker.data.settings

import android.app.Application
import android.content.Context
import android.util.Log
import com.example.playlistmaker.app.App
import com.example.playlistmaker.data.settings.dto.ThemeCode
import com.example.playlistmaker.domain.settings.SettingsRepository
import com.google.gson.Gson

class SettingsRepoImpl(private val application: Application) : SettingsRepository {
    private val sharedPrefs = application.applicationContext.getSharedPreferences(App.PLAYLIST_MAKER, Context.MODE_PRIVATE)
    private val gson = Gson()

    override fun switchTheme(checked: Boolean) {
        Log.wtf("SWITCH", "In repository switched")
        application as App
        application.switchTheme(checked)
        store(App.THEME, ThemeCode(if (checked) ThemeCode.NIGHT_MODE_CODE else ThemeCode.DAY_MODE_CODE))
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