package com.example.playlistmaker.domain.storage.use_cases_impl.id

import com.example.playlistmaker.domain.storage.repos.basic_repos.GetDataRepo
import com.example.playlistmaker.domain.storage.use_cases.GetItemUseCase
import kotlinx.coroutines.flow.Flow

class GetIdUseCase(private val repository: GetDataRepo<Long>) : GetItemUseCase<Long> {
    override suspend fun get(): Flow<Long?> = repository.get()
}