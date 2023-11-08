package com.example.playlistmaker.domain.storage.use_cases

interface DeleteItemUseCase<T> {
    fun delete(item: T)
}