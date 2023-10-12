package com.example.playlistmaker.domain.db

import com.example.playlistmaker.domain.storage.StorageManagerRepo
import kotlinx.coroutines.flow.Flow

interface DbRepo<T> : StorageManagerRepo<T> {
    fun delete(item: T)

    fun getById(id: Long): Flow<T?>

}