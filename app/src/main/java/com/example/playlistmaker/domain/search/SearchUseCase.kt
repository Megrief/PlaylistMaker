package com.example.playlistmaker.domain.search

import com.example.playlistmaker.domain.entity.Track
import kotlinx.coroutines.flow.Flow

interface SearchUseCase {
    suspend fun search(term: String): Flow<List<Track>?>
}