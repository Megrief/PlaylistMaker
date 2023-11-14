package com.example.playlistmaker.domain.storage.use_cases_impl.playlist

import com.example.playlistmaker.domain.entities.Playlist
import com.example.playlistmaker.domain.storage.repos.basic_repos.GetDataByIdRepo
import com.example.playlistmaker.domain.storage.use_cases.GetItemByIdUseCase
import kotlinx.coroutines.flow.Flow

class GetPlaylistByIdUseCase(private val repository: GetDataByIdRepo<Playlist>) : GetItemByIdUseCase<Playlist> {

    override fun get(id: Long): Flow<Playlist?> = repository.getById(id)
}