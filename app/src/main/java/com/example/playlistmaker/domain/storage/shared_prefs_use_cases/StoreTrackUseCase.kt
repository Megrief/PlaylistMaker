package com.example.playlistmaker.domain.storage.shared_prefs_use_cases

import com.example.playlistmaker.domain.entity.Track
import com.example.playlistmaker.domain.storage.StorageManagerRepo
import com.example.playlistmaker.domain.storage.use_cases.StoreDataUseCase

class StoreTrackUseCase(private val repository: StorageManagerRepo<Track?>) : StoreDataUseCase<Track> {

    override fun store(item: Track) {
        repository.store(item)
    }
}