package com.example.playlistmaker.domain.storage.use_cases_impl.playlist

import com.example.playlistmaker.domain.entities.Playlist
import com.example.playlistmaker.domain.storage.repos.basic_repos.StoreDataRepo
import com.example.playlistmaker.domain.storage.use_cases.StoreItemUseCase

class StorePlaylistInDbUseCaseImpl(private val repository: StoreDataRepo<Playlist>) : StoreItemUseCase<Playlist> {

    override fun store(item: Playlist): Boolean = repository.store(item)
}