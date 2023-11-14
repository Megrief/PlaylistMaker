package com.example.playlistmaker.data.storage.shared_prefs.repo_impl

import android.content.SharedPreferences
import com.example.playlistmaker.data.storage.shared_prefs.dto.TrackListDto
import com.example.playlistmaker.domain.entities.Track
import com.example.playlistmaker.domain.storage.repos.StorageManager
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class SharedPrefsList(
    private val sharedPrefs: SharedPreferences,
    private val gson: Gson
) : StorageManager<List<Track>> {

    override fun store(item: List<Track>): Boolean =
        gson.toJson(TrackListDto(item)).let { trackListDtoJson ->
            sharedPrefs.edit().putString(HISTORY_KEY, trackListDtoJson).commit()
        }

    override fun get(): Flow<List<Track>> {
        return flow {
            sharedPrefs.getString(HISTORY_KEY, null)?.let { trackListDtoJson ->
                emit(gson.fromJson(trackListDtoJson, TrackListDto::class.java).list)
            } ?: emit(emptyList())
        }
    }

    companion object {
        private const val HISTORY_KEY = "HISTORY_KEY"
    }
}