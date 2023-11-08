package com.example.playlistmaker.domain.storage.shared_prefs_use_cases

import com.example.playlistmaker.domain.storage.repos.basic_repos.StoreDataRepo
import com.example.playlistmaker.domain.storage.use_cases.StoreItemUseCase

class StorePhotoIdUseCase(private val repository: StoreDataRepo<Long>) : StoreItemUseCase<Long> {
    override fun store(item: Long): Boolean = repository.store(item)

}