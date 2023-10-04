package com.example.playlistmaker.domain.search.use_cases_impl

import com.example.playlistmaker.domain.entity.Track
import com.example.playlistmaker.domain.search.SearchRepository
import com.example.playlistmaker.domain.storage.use_cases.GetDataUseCase
import kotlinx.coroutines.flow.Flow

class SearchUseCaseImpl(private val repository: SearchRepository) : GetDataUseCase<List<Track>?> {

    override suspend fun get(key: String): Flow<List<Track>?> {
        return repository.search(key)
    }
}