package com.example.playlistmaker.domain.storage.external_storage.use_cases

interface StoreItemWithIdUseCase<T> {
    fun storeWithId(item: T, id: Long)
}