package com.example.playlistmaker.domain.storage

import kotlinx.coroutines.flow.Flow

interface StorageManagerRepo<T> {
    fun store(item: T)

    fun get(): Flow<T>
}