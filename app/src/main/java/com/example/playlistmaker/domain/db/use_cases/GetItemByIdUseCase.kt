package com.example.playlistmaker.domain.db.use_cases

import kotlinx.coroutines.flow.Flow

interface GetItemByIdUseCase<T> {
    fun get(id: Long): Flow<T?>
}