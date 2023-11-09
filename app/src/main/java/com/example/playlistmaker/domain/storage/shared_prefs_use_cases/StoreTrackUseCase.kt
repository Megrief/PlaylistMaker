package com.example.playlistmaker.domain.storage.shared_prefs_use_cases

import com.example.playlistmaker.domain.entities.Track
import com.example.playlistmaker.domain.storage.repos.basic_repos.StoreDataRepo
import com.example.playlistmaker.domain.storage.use_cases.StoreItemUseCase

class StoreTrackUseCase(private val repository: StoreDataRepo<Track>) : StoreItemUseCase<Track> {

    override fun store(item: Track): Boolean = repository.store(item)
}