package com.example.playlistmaker.domain.storage.use_cases

interface StoreDataUseCase<T> {
    fun store(item: T)
}