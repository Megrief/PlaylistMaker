package com.example.playlistmaker.domain.media.playlists.use_cases_impl

import android.net.Uri
import com.example.playlistmaker.domain.storage.repos.basic_repos.StoreDataRepo
import com.example.playlistmaker.domain.storage.use_cases.StoreItemUseCase

class StorePhotoUseCaseImpl(private val repository: StoreDataRepo<Uri>) : StoreItemUseCase<Uri> {

    override fun store(item: Uri): Boolean = repository.store(item)
}