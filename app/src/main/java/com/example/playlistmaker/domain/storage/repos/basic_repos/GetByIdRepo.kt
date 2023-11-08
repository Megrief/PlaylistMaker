package com.example.playlistmaker.domain.storage.repos.basic_repos

import kotlinx.coroutines.flow.Flow

interface GetByIdRepo<T> {
    fun getById(id: Long): Flow<T?>
}