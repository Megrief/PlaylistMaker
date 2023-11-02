package com.example.playlistmaker.domain.media.playlists.use_cases_impl

import com.example.playlistmaker.domain.entities.Playlist
import com.example.playlistmaker.domain.storage.StorageManagerRepo
import com.example.playlistmaker.domain.storage.use_cases.StoreDataUseCase

class StorePlaylistInDbUseCaseImpl(private val repository: StorageManagerRepo<Playlist>) : StoreDataUseCase<Playlist> {

    override fun store(item: Playlist) {
        repository.store(item)
    }
}