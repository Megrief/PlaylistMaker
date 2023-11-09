package com.example.playlistmaker.domain.media.playlists.use_cases_impl

import com.example.playlistmaker.domain.entities.Playlist
import com.example.playlistmaker.domain.storage.repos.basic_repos.GetDataRepo
import com.example.playlistmaker.domain.storage.use_cases.GetItemUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.toList

class GetPlaylistsUseCaseImpl(private val repository: GetDataRepo<Playlist>) : GetItemUseCase<List<Playlist>> {

    override suspend fun get(): Flow<List<Playlist>> = flow {
        emit(repository.get().filterNotNull().toList())
    }
}