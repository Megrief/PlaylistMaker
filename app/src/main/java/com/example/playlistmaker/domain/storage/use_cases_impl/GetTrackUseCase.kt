package com.example.playlistmaker.domain.storage.use_cases_impl

import android.util.Log
import com.example.playlistmaker.domain.entity.Track
import com.example.playlistmaker.domain.storage.StorageManagerRepo
import com.example.playlistmaker.domain.storage.use_cases.GetDataUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.single

class GetTrackUseCase(
    private val repository: StorageManagerRepo<Track?>
) : GetDataUseCase<Track?> {

    override suspend fun get(): Flow<Track?> {
        val res = repository.get()
        Log.d("DEBUG", "in getUseCase ${ res.single()?.previewUrl != null }")
        return res
    }
}