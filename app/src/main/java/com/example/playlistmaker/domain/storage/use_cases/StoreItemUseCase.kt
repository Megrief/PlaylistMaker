package com.example.playlistmaker.domain.storage.use_cases

interface StoreItemUseCase<T> {
    fun store(item: T): Boolean
}