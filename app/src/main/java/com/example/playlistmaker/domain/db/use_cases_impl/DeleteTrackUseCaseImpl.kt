package com.example.playlistmaker.domain.db.use_cases_impl

import com.example.playlistmaker.domain.db.DbRepo
import com.example.playlistmaker.domain.db.use_cases.DeleteItemUseCase
import com.example.playlistmaker.domain.entity.Track

class DeleteTrackUseCaseImpl(private val repository: DbRepo<Track>) : DeleteItemUseCase {
    override fun delete(id: Long) {
        repository.delete(id)
    }
}