package com.example.playlistmaker.domain.use_cases

interface StoreDataUseCase<T> {
    fun store(key: String, item: T)
}