package com.example.playlistmaker.domain.media.playlists.use_cases_impl

import com.example.playlistmaker.domain.media.entity.Playlist
import com.example.playlistmaker.domain.storage.StorageManagerRepo
import com.example.playlistmaker.domain.storage.use_cases.GetDataUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.toList

class GetPlaylistsUseCaseImpl(private val repository: StorageManagerRepo<Playlist>) : GetDataUseCase<List<Playlist>> {

    override suspend fun get(): Flow<List<Playlist>> = flow {
        emit(repository.get().toList())
    }
}