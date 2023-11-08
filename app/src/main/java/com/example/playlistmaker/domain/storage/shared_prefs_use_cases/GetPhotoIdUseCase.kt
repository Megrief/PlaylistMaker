package com.example.playlistmaker.domain.storage.shared_prefs_use_cases

import com.example.playlistmaker.domain.storage.repos.basic_repos.GetDataRepo
import com.example.playlistmaker.domain.storage.use_cases.GetItemUseCase
import kotlinx.coroutines.flow.Flow

class GetPhotoIdUseCase(private val repository: GetDataRepo<Long>) : GetItemUseCase<Long> {
    override suspend fun get(): Flow<Long?> = repository.get()
}