package com.example.playlistmaker.domain.search

import com.example.playlistmaker.domain.entities.Track
import kotlinx.coroutines.flow.Flow

interface SearchRepository {
    suspend fun search(term: String): Flow<List<Track>?>
}