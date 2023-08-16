package com.example.playlistmaker.domain.storage.use_cases_impl

import com.example.playlistmaker.domain.entities.Track
import com.example.playlistmaker.domain.storage.StorageManagerRepo
import com.example.playlistmaker.domain.storage.use_cases.StoreDataUseCase

class StoreTrackUseCase(private val repository: StorageManagerRepo<Track?>) :
    StoreDataUseCase<Track> {

    override fun store(key: String, item: Track) {
        repository.store(key, item)
    }
}