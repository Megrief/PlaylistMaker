package com.example.playlistmaker.domain.media.use_cases_impl

import com.example.playlistmaker.domain.media.entity.Playlist
import com.example.playlistmaker.domain.storage.StorageManagerRepo
import com.example.playlistmaker.domain.storage.use_cases.StoreDataUseCase

class StorePlaylistUseCase(
    private val repository: StorageManagerRepo<Playlist?>
) : StoreDataUseCase<Playlist> {

    override fun store(key: String, item: Playlist) {
        repository.store(key, item)
    }
}