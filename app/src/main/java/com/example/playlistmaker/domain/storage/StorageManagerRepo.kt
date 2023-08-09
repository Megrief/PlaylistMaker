package com.example.playlistmaker.domain.storage

interface StorageManagerRepo<T> {
    fun store(key: String, item: T)

    fun get(key: String): T
}