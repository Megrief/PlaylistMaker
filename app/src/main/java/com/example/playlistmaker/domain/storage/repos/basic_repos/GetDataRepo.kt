package com.example.playlistmaker.domain.storage.repos.basic_repos

import kotlinx.coroutines.flow.Flow

interface GetDataRepo<T> {
    fun get(): Flow<T?>
}