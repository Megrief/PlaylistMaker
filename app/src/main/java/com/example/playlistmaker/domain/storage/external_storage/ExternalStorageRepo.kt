package com.example.playlistmaker.domain.storage.external_storage

import kotlinx.coroutines.flow.Flow
import java.io.File

interface ExternalStorageRepo<T> {

    fun getByName(name: String): Flow<T?>
    fun store(item: T): File
}