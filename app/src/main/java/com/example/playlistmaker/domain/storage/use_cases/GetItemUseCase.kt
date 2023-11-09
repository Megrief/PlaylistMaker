package com.example.playlistmaker.domain.storage.use_cases

import kotlinx.coroutines.flow.Flow

interface GetItemUseCase<T> {
    suspend fun get(): Flow<T?>
}