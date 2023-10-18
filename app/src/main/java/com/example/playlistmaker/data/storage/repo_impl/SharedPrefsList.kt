package com.example.playlistmaker.data.storage.repo_impl

import android.content.SharedPreferences
import com.example.playlistmaker.data.storage.dto.TrackListDto
import com.example.playlistmaker.domain.entity.Track
import com.example.playlistmaker.domain.storage.StorageManagerRepo
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class SharedPrefsList(
    private val sharedPrefs: SharedPreferences,
    private val gson: Gson
) : StorageManagerRepo<List<Track>> {

    override fun store(item: List<Track>) {
        gson.toJson(TrackListDto(item)).also { trackListDtoJson ->
            sharedPrefs.edit().putString(HISTORY_KEY, trackListDtoJson).apply()
        }
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