package com.example.playlistmaker.domain.db.use_cases

interface DeleteItemUseCase<T> {
    fun delete(item: T)
}