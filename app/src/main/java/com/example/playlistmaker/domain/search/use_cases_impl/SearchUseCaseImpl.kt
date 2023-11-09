package com.example.playlistmaker.domain.search.use_cases_impl

import com.example.playlistmaker.domain.entities.Track
import com.example.playlistmaker.domain.search.SearchRepository
import com.example.playlistmaker.domain.search.SearchUseCase
import kotlinx.coroutines.flow.Flow

class SearchUseCaseImpl(private val repository: SearchRepository) : SearchUseCase {
    override suspend fun search(term: String): Flow<List<Track>?> = repository.search(term)
}