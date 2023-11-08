package com.example.playlistmaker.domain.settings.use_cases_impl

import com.example.playlistmaker.domain.settings.entity.ThemeFlag
import com.example.playlistmaker.domain.storage.repos.basic_repos.StoreDataRepo
import com.example.playlistmaker.domain.storage.use_cases.StoreItemUseCase

class StoreThemeFlagUseCase(private val repository: StoreDataRepo<ThemeFlag?>) : StoreItemUseCase<ThemeFlag> {

    override fun store(item: ThemeFlag): Boolean = repository.store(item)
}