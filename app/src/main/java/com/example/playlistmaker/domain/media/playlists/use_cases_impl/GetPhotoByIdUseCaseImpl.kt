package com.example.playlistmaker.domain.media.playlists.use_cases_impl

import android.net.Uri
import com.example.playlistmaker.domain.storage.repos.basic_repos.GetByIdRepo
import com.example.playlistmaker.domain.storage.use_cases.GetItemByIdUseCase
import kotlinx.coroutines.flow.Flow

class GetPhotoByIdUseCaseImpl(private val repository: GetByIdRepo<Uri>) : GetItemByIdUseCase<Uri> {

    override fun get(id: Long): Flow<Uri?> = repository.getById(id)

}