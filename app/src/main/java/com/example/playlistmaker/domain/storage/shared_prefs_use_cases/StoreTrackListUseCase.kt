package com.example.playlistmaker.domain.storage.shared_prefs_use_cases

import com.example.playlistmaker.domain.entities.Track
import com.example.playlistmaker.domain.storage.StorageManagerRepo
import com.example.playlistmaker.domain.storage.use_cases.StoreDataUseCase

class StoreTrackListUseCase(private val repository: StorageManagerRepo<List<Track>>) : StoreDataUseCase<List<Track>> {

    override fun store(item: List<Track>) {
        repository.store(item)
    }
}