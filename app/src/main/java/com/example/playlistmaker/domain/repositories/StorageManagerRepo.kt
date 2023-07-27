package com.example.playlistmaker.domain.repositories

interface StorageManagerRepo<T> {
    fun save(key: String, item: T)

    fun get(key: String): T
}