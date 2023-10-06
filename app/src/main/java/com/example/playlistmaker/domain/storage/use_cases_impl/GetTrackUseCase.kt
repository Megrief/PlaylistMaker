package com.example.playlistmaker.domain.storage.use_cases_impl

import com.example.playlistmaker.domain.entity.Track
import com.example.playlistmaker.domain.storage.StorageManagerRepo
import com.example.playlistmaker.domain.storage.use_cases.GetDataUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetTrackUseCase(
    private val repository: StorageManagerRepo<Track?>
) : GetDataUseCase<Track?> {

    override suspend fun get(key: String): Flow<Track?> {
        return flow {
            emit(repository.get(key))
        }
    }
}