package com.example.playlistmaker.data.storage.repo_impl

import android.content.Context
import com.example.playlistmaker.app.App
import com.example.playlistmaker.data.storage.dto.TrackListDto
import com.example.playlistmaker.domain.entities.Track
import com.example.playlistmaker.domain.storage.StorageManagerRepo
import com.google.gson.Gson

class SharedPrefsList(context: Context) : StorageManagerRepo<List<Track>> {
    private val sharedPrefs = context.getSharedPreferences(App.PLAYLIST_MAKER, Context.MODE_PRIVATE)
    private val gson = Gson()

    override fun store(key: String, item: List<Track>) {
        gson.toJson(TrackListDto(item)).let {
            sharedPrefs.edit().putString(key, it).apply()
        }
    }

    override fun get(key: String): List<Track> {
        return sharedPrefs.getString(key, null)?.let {
            gson.fromJson(it, TrackListDto::class.java).list
        } ?: emptyList()
    }
}