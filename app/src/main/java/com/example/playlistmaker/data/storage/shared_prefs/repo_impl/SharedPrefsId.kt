package com.example.playlistmaker.data.storage.shared_prefs.repo_impl

import android.content.SharedPreferences
import com.example.playlistmaker.domain.storage.repos.StorageManagerRepo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class SharedPrefsId(
    private val sharedPrefs: SharedPreferences
) : StorageManagerRepo<Long> {

    override fun get(): Flow<Long?> = flow {
        sharedPrefs.getLong(KEY, 0).also {
            emit(it)
        }
    }

    override fun store(item: Long): Boolean = sharedPrefs.edit().putLong(KEY, item).commit()

    companion object {
        private const val KEY = "SharedPrefsIdKey"
    }
}