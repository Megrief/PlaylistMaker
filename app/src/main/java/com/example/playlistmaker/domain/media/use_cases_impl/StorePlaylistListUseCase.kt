package com.example.playlistmaker.domain.media.use_cases_impl

import com.example.playlistmaker.domain.media.entity.Playlist
import com.example.playlistmaker.domain.storage.StorageManagerRepo
import com.example.playlistmaker.domain.storage.use_cases.StoreDataUseCase

class StorePlaylistListUseCase(
    private val repository: StorageManagerRepo<List<Playlist>>
) : StoreDataUseCase<List<Playlist>> {

    override fun store(key: String, item: List<Playlist>) {
        repository.store(key, item)
    }
}