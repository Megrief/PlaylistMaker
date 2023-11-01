package com.example.playlistmaker.domain.storage.db.use_cases

interface DeleteItemUseCase {
    fun delete(id: Long)
}