package com.example.playlistmaker.domain.storage.shared_prefs_use_cases

import com.example.playlistmaker.domain.entities.Track
import com.example.playlistmaker.domain.storage.StorageManagerRepo
import com.example.playlistmaker.domain.storage.use_cases.GetDataUseCase
import kotlinx.coroutines.flow.Flow

class GetTrackListUseCase(private val repository: StorageManagerRepo<List<Track>>) : GetDataUseCase<List<Track>> {
    override suspend fun get(): Flow<List<Track>> = repository.get()
}