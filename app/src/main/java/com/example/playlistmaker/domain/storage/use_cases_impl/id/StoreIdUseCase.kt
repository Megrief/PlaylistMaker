package com.example.playlistmaker.domain.storage.use_cases_impl.id

import com.example.playlistmaker.domain.storage.repos.basic_repos.StoreDataRepo
import com.example.playlistmaker.domain.storage.use_cases.StoreItemUseCase

class StoreIdUseCase(private val repository: StoreDataRepo<Long>) : StoreItemUseCase<Long> {
    override fun store(item: Long): Boolean = repository.store(item)

}