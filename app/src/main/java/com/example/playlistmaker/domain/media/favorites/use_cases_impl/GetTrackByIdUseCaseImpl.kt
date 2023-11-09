package com.example.playlistmaker.domain.media.favorites.use_cases_impl

import com.example.playlistmaker.domain.entities.Track
import com.example.playlistmaker.domain.storage.repos.basic_repos.GetByIdRepo
import com.example.playlistmaker.domain.storage.use_cases.GetItemByIdUseCase
import kotlinx.coroutines.flow.Flow

class GetTrackByIdUseCaseImpl(private val repository: GetByIdRepo<Track>) : GetItemByIdUseCase<Track> {
    override fun get(id: Long): Flow<Track?> = repository.getById(id)
}