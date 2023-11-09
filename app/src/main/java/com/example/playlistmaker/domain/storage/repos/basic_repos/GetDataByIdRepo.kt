package com.example.playlistmaker.domain.storage.repos.basic_repos

import kotlinx.coroutines.flow.Flow

interface GetDataByIdRepo<T> {
    fun getById(id: Long): Flow<T?>
}