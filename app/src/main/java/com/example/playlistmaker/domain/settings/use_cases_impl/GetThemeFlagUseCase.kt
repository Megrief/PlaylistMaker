package com.example.playlistmaker.domain.settings.use_cases_impl

import com.example.playlistmaker.domain.settings.entity.ThemeFlag
import com.example.playlistmaker.domain.storage.StorageManagerRepo
import com.example.playlistmaker.domain.storage.use_cases.GetDataUseCase
import java.util.function.Consumer

class GetThemeFlagUseCase(private val repository: StorageManagerRepo<ThemeFlag?>) :
    GetDataUseCase<ThemeFlag?> {

    override fun get(key: String, consumer: Consumer<ThemeFlag?>) {
        consumer.accept(repository.get(key))
    }
}