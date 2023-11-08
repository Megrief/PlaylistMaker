package com.example.playlistmaker.domain.storage.use_cases

import kotlinx.coroutines.flow.Flow

interface GetItemByIdUseCase<T> {
    fun get(id: Long): Flow<T?>
}