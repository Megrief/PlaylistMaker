package com.example.playlistmaker.domain.media.playlists.use_cases_impl

import com.example.playlistmaker.domain.entities.Playlist
import com.example.playlistmaker.domain.storage.repos.basic_repos.GetByIdRepo
import com.example.playlistmaker.domain.storage.use_cases.GetItemByIdUseCase
import kotlinx.coroutines.flow.Flow

class GetPlaylistByIdUseCase(private val repository: GetByIdRepo<Playlist>) : GetItemByIdUseCase<Playlist> {

    override fun get(id: Long): Flow<Playlist?> = repository.getById(id)
}