package com.example.playlistmaker.domain.storage.use_cases_impl

import com.example.playlistmaker.domain.entity.Track
import com.example.playlistmaker.domain.storage.StorageManagerRepo
import com.example.playlistmaker.domain.storage.use_cases.StoreDataUseCase

class StoreTrackListUseCase(
    private val repository: StorageManagerRepo<List<Track>>
) : StoreDataUseCase<List<Track>> {

    override fun store(key: String, item: List<Track>) {
        repository.store(key, item)
    }
}