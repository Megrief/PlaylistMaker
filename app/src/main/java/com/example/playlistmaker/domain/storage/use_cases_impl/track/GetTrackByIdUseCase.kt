package com.example.playlistmaker.domain.storage.use_cases_impl.track

import com.example.playlistmaker.domain.entities.Track
import com.example.playlistmaker.domain.storage.repos.basic_repos.GetDataByIdRepo
import com.example.playlistmaker.domain.storage.use_cases.GetItemByIdUseCase
import kotlinx.coroutines.flow.Flow

class GetTrackByIdUseCase(private val repository: GetDataByIdRepo<Track>) : GetItemByIdUseCase<Track> {
    override fun get(id: Long): Flow<Track?> = repository.getById(id)
}