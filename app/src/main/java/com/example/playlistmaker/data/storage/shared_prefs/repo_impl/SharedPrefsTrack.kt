package com.example.playlistmaker.data.storage.shared_prefs.repo_impl

import android.content.SharedPreferences
import com.example.playlistmaker.domain.entities.Track
import com.example.playlistmaker.domain.storage.StorageManagerRepo
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class SharedPrefsTrack(
    private val sharedPrefs: SharedPreferences,
    private val gson: Gson
) : StorageManagerRepo<Track?> {
    override fun store(item: Track?) {
        gson.toJson(item, Track::class.java).also { trackJson ->
            sharedPrefs.edit().putString(TRACK, trackJson).apply()
        }
    }

    override fun get(): Flow<Track?> {
        return flow {
            sharedPrefs.getString(TRACK, null)?.let { trackJson ->
                emit(gson.fromJson(trackJson, Track::class.java))
            }
        }
    }

    companion object {
        private const val TRACK = "TRACK"
    }
}