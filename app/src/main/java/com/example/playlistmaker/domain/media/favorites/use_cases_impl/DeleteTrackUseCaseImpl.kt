package com.example.playlistmaker.domain.media.favorites.use_cases_impl

import com.example.playlistmaker.domain.entities.Track
import com.example.playlistmaker.domain.storage.repos.basic_repos.DeleteDataRepo
import com.example.playlistmaker.domain.storage.use_cases.DeleteItemUseCase

class DeleteTrackUseCaseImpl(private val repository: DeleteDataRepo<Track>) : DeleteItemUseCase<Track> {
    override fun delete(item: Track) {
        repository.delete(item)
    }
}