package com.example.playlistmaker.domain.db.use_cases_impl

import com.example.playlistmaker.domain.db.DbRepo
import com.example.playlistmaker.domain.db.use_cases.GetItemByIdUseCase
import com.example.playlistmaker.domain.entity.Track
import kotlinx.coroutines.flow.Flow

class GetTrackByIdUseCaseImpl(private val repository: DbRepo<Track>) : GetItemByIdUseCase<Track> {
    override fun get(id: Long): Flow<Track?> = repository.getById(id)
}