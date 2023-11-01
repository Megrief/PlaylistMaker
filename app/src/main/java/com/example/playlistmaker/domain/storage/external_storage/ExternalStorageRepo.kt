package com.example.playlistmaker.domain.storage.external_storage

import kotlinx.coroutines.flow.Flow

interface ExternalStorageRepo<T> {

    fun getByName(name: String): Flow<T?>
    fun store(item: T): String
}