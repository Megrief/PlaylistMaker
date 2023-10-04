package com.example.playlistmaker.domain.media.use_cases_impl

import com.example.playlistmaker.domain.media.entity.Playlist
import com.example.playlistmaker.domain.storage.StorageManagerRepo
import com.example.playlistmaker.domain.storage.use_cases.GetDataUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetPlaylistListUseCase(
    private val repository: StorageManagerRepo<List<Playlist>>
) : GetDataUseCase<List<Playlist>> {

    override suspend fun get(key: String): Flow<List<Playlist>> {
        return flow {
            repository.get(key)
        }
    }
}