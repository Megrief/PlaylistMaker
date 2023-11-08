package com.example.playlistmaker.domain.settings.use_cases_impl

import com.example.playlistmaker.domain.settings.entity.ThemeFlag
import com.example.playlistmaker.domain.storage.repos.basic_repos.GetDataRepo
import com.example.playlistmaker.domain.storage.use_cases.GetItemUseCase
import kotlinx.coroutines.flow.Flow

class GetThemeFlagUseCase(private val repository: GetDataRepo<ThemeFlag?>) : GetItemUseCase<ThemeFlag?> {
    override suspend fun get(): Flow<ThemeFlag?> = repository.get()
}
