package com.example.playlistmaker.domain.storage.use_cases_impl.playlist

import com.example.playlistmaker.domain.entities.Playlist
import com.example.playlistmaker.domain.storage.repos.basic_repos.DeleteDataRepo
import com.example.playlistmaker.domain.storage.use_cases.DeleteItemUseCase

class DeletePlaylistUseCase(private val repository: DeleteDataRepo<Playlist>) : DeleteItemUseCase<Playlist> {

    override fun delete(item: Playlist) {
        repository.delete(item)
    }
}