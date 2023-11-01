package com.example.playlistmaker.domain.media.favorites.use_cases_impl

import com.example.playlistmaker.domain.entity.Track
import com.example.playlistmaker.domain.storage.StorageManagerRepo
import com.example.playlistmaker.domain.storage.use_cases.StoreDataUseCase

class StoreTrackInDbUseCaseImpl(private val repository: StorageManagerRepo<Track>) : StoreDataUseCase<Track> {
    override fun store(item: Track) {
        repository.store(item)
    }
}