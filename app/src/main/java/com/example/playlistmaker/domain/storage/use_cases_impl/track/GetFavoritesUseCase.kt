package com.example.playlistmaker.domain.storage.use_cases_impl.track

import com.example.playlistmaker.domain.entities.Track
import com.example.playlistmaker.domain.storage.repos.basic_repos.GetDataRepo
import com.example.playlistmaker.domain.storage.use_cases.GetItemUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.toList

class GetFavoritesUseCase(private val repository: GetDataRepo<Track>) : GetItemUseCase<List<Track>> {
    override suspend fun get(): Flow<List<Track>> = flow {
        emit(repository.get().filterNotNull().toList())
    }

}