package com.example.playlistmaker.data.media.repo_impl

import android.content.SharedPreferences
import com.example.playlistmaker.domain.media.entity.Playlist
import com.example.playlistmaker.domain.storage.StorageManagerRepo
import com.google.gson.Gson

class SharedPrefsPlaylist(
    private val sharedPrefs: SharedPreferences,
    private val gson: Gson
) : StorageManagerRepo<Playlist?> {

    override fun store(key: String, item: Playlist?) {
        gson.toJson(item).also { playlistJson ->
            sharedPrefs.edit().putString(key, playlistJson).apply()
        }
    }

    override fun get(key: String): Playlist? {
        return sharedPrefs.getString(key, null)?.let { playlistJson ->
            gson.fromJson(playlistJson, Playlist::class.java)
        }
    }
}