package com.example.playlistmaker.domain.storage.use_cases

import kotlinx.coroutines.flow.Flow

interface GetDataUseCase<T> {
    suspend fun get(): Flow<T>
}