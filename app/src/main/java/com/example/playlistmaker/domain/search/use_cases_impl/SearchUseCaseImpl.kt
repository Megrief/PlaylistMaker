package com.example.playlistmaker.domain.search.use_cases_impl

import com.example.playlistmaker.domain.entity.Track
import com.example.playlistmaker.domain.search.SearchRepository
import com.example.playlistmaker.domain.search.SearchUseCase
import kotlinx.coroutines.flow.Flow

class SearchUseCaseImpl(private val repository: SearchRepository) : SearchUseCase {

    override suspend fun search(term: String): Flow<List<Track>?> {
        return repository.search(term)
    }
}