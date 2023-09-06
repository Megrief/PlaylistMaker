package com.example.playlistmaker.domain.settings.use_cases_impl

import com.example.playlistmaker.domain.settings.entity.ThemeFlag
import com.example.playlistmaker.domain.storage.StorageManagerRepo
import com.example.playlistmaker.domain.storage.use_cases.StoreDataUseCase

class StoreThemeFlagUseCase(
    private val repository: StorageManagerRepo<ThemeFlag?>
) : StoreDataUseCase<ThemeFlag> {

    override fun store(key: String, item: ThemeFlag) {
        repository.store(key, item)
    }
}