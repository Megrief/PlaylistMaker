package com.example.playlistmaker.domain.storage.use_cases

interface StoreDataUseCase<T> {
    fun store(key: String, item: T)
}