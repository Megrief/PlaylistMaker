package com.example.playlistmaker.domain.settings.use_cases_impl

import com.example.playlistmaker.data.settings.dto.ThemeCode
import com.example.playlistmaker.domain.storage.StorageManagerRepo
import com.example.playlistmaker.domain.use_cases.GetDataUseCase
import java.util.function.Consumer

class GetThemeCodeUseCase(private val repository: StorageManagerRepo<ThemeCode?>) : GetDataUseCase<ThemeCode?> {

    override fun get(key: String, consumer: Consumer<ThemeCode?>) {
        consumer.accept(repository.get(key))
    }
}