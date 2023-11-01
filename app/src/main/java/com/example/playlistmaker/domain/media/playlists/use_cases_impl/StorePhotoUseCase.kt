package com.example.playlistmaker.domain.media.playlists.use_cases_impl

import android.net.Uri
import com.example.playlistmaker.domain.storage.external_storage.ExternalStorageRepo

class StorePhotoUseCase(private val repository: ExternalStorageRepo<Uri>) {

    fun store(item: Uri): String = repository.store(item)
}