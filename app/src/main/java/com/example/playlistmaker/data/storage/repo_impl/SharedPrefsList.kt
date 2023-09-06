package com.example.playlistmaker.data.storage.repo_impl

import android.content.SharedPreferences
import com.example.playlistmaker.data.storage.dto.TrackListDto
import com.example.playlistmaker.domain.entity.Track
import com.example.playlistmaker.domain.storage.StorageManagerRepo
import com.google.gson.Gson

class SharedPrefsList(
    private val sharedPrefs: SharedPreferences,
    private val gson: Gson
) : StorageManagerRepo<List<Track>> {

    override fun store(key: String, item: List<Track>) {
        gson.toJson(TrackListDto(item)).also { trackListDtoJson ->
            sharedPrefs.edit().putString(key, trackListDtoJson).apply()
        }
    }

    override fun get(key: String): List<Track> {
        return sharedPrefs.getString(key, null)?.let { trackListDtoJson ->
            gson.fromJson(trackListDtoJson, TrackListDto::class.java).list
        } ?: emptyList()
    }
}