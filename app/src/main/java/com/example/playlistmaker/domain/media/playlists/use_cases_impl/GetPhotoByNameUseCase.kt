package com.example.playlistmaker.domain.media.playlists.use_cases_impl

import android.net.Uri
import com.example.playlistmaker.domain.storage.external_storage.ExternalStorageRepo
import kotlinx.coroutines.flow.Flow

class GetPhotoByNameUseCase(private val repository: ExternalStorageRepo<Uri>) {

    fun getByName(name: String): Flow<Uri?> = repository.getByName(name)
}