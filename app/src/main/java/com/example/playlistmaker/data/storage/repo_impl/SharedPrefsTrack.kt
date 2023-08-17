package com.example.playlistmaker.data.storage.repo_impl

import android.content.SharedPreferences
import com.example.playlistmaker.domain.entities.Track
import com.example.playlistmaker.domain.storage.StorageManagerRepo
import com.google.gson.Gson

class SharedPrefsTrack(private val sharedPrefs: SharedPreferences, private val gson: Gson) : StorageManagerRepo<Track?> {
    override fun store(key: String, item: Track?) {
        gson.toJson(item).let {
            sharedPrefs.edit().putString(key, it).apply()
        }
    }

    override fun get(key: String): Track? {
        return sharedPrefs.getString(key, null)?.let {
             gson.fromJson(it, Track::class.java)
        }
    }
}