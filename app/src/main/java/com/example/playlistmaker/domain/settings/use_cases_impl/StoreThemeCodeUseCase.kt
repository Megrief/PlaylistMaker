package com.example.playlistmaker.domain.settings.use_cases_impl

import com.example.playlistmaker.data.settings.dto.ThemeCode
import com.example.playlistmaker.domain.storage.StorageManagerRepo
import com.example.playlistmaker.domain.storage.use_cases.StoreDataUseCase

class StoreThemeCodeUseCase(private val repository: StorageManagerRepo<ThemeCode?>) :
    StoreDataUseCase<ThemeCode> {

    override fun store(key: String, item: ThemeCode) {
        repository.store(key, item)
    }
}