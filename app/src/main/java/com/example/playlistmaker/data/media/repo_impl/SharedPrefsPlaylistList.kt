package com.example.playlistmaker.data.media.repo_impl

import android.content.SharedPreferences
import com.example.playlistmaker.data.media.dto.PlaylistsDto
import com.example.playlistmaker.domain.media.entity.Playlist
import com.example.playlistmaker.domain.storage.StorageManagerRepo
import com.google.gson.Gson

class SharedPrefsPlaylistList(
    private val sharedPrefs: SharedPreferences,
    private val gson: Gson
) : StorageManagerRepo<List<Playlist>> {

    override fun store(key: String, item: List<Playlist>) {
        gson.toJson(PlaylistsDto(item)).also { playlistsDto ->
            sharedPrefs.edit().putString(key, playlistsDto).apply()
        }
    }

    override fun get(key: String): List<Playlist> {
        return sharedPrefs.getString(key, null)?.let { playlistsDtoJson ->
            gson.fromJson(playlistsDtoJson, PlaylistsDto::class.java).playlists
        } ?: emptyList()
    }
}