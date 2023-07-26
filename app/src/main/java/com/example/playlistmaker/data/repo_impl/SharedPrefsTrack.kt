package com.example.playlistmaker.data.repo_impl

import android.content.Context
import com.example.playlistmaker.app.App
import com.example.playlistmaker.domain.entities.Track
import com.example.playlistmaker.domain.repositories.StorageManagerRepo
import com.google.gson.Gson

class SharedPrefsTrack(context: Context) : StorageManagerRepo<Track?> {
    private val sharedPrefs = context.getSharedPreferences(App.PLAYLIST_MAKER, Context.MODE_PRIVATE)

    override fun save(key: String, item: Track?) {
        Gson().toJson(item).let {
            sharedPrefs.edit().putString(key, it).apply()
        }
    }

    override fun get(key: String): Track? {
        return sharedPrefs.getString(key, null)?.let {
             Gson().fromJson(it, Track::class.java)
        }
    }
}