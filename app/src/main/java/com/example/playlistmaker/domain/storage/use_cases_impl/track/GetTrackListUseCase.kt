package com.example.playlistmaker.domain.storage.use_cases_impl.track

import com.example.playlistmaker.domain.entities.Track
import com.example.playlistmaker.domain.storage.repos.basic_repos.GetDataRepo
import com.example.playlistmaker.domain.storage.use_cases.GetItemUseCase
import kotlinx.coroutines.flow.Flow

class GetTrackListUseCase(private val repository: GetDataRepo<List<Track>>) : GetItemUseCase<List<Track>> {
    override suspend fun get(): Flow<List<Track>?> = repository.get()
}