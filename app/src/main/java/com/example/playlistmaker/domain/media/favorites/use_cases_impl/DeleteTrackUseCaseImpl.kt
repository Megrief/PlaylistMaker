package com.example.playlistmaker.domain.media.favorites.use_cases_impl

import com.example.playlistmaker.domain.storage.db.DbRepo
import com.example.playlistmaker.domain.storage.db.use_cases.DeleteItemUseCase
import com.example.playlistmaker.domain.entities.Track

class DeleteTrackUseCaseImpl(private val repository: DbRepo<Track>) : DeleteItemUseCase {
    override fun delete(id: Long) {
        repository.delete(id)
    }
}