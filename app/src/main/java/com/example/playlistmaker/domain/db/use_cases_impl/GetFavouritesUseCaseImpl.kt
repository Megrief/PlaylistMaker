package com.example.playlistmaker.domain.db.use_cases_impl

import com.example.playlistmaker.domain.entity.Track
import com.example.playlistmaker.domain.storage.StorageManagerRepo
import com.example.playlistmaker.domain.storage.use_cases.GetDataUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.toList

class GetFavouritesUseCaseImpl(private val repository: StorageManagerRepo<Track>) : GetDataUseCase<List<Track>> {
    override suspend fun get(): Flow<List<Track>> {
        return flow {
            emit(repository.get().toList())
        }
    }

}