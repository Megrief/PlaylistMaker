package com.example.playlistmaker.domain.settings.use_cases_impl

import com.example.playlistmaker.domain.settings.entity.ThemeFlag
import com.example.playlistmaker.domain.storage.StorageManagerRepo
import com.example.playlistmaker.domain.storage.use_cases.GetDataUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetThemeFlagUseCase(
    private val repository: StorageManagerRepo<ThemeFlag?>
) : GetDataUseCase<ThemeFlag?> {

    override suspend fun get(key: String): Flow<ThemeFlag?> {
        return flow {
            emit(repository.get(key))
        }
    }
}