package com.example.playlistmaker.domain.storage.use_cases_impl.photo_uri

import android.net.Uri
import com.example.playlistmaker.domain.storage.repos.basic_repos.GetDataByIdRepo
import com.example.playlistmaker.domain.storage.use_cases.GetItemByIdUseCase
import kotlinx.coroutines.flow.Flow

class GetPhotoByIdUseCaseImpl(private val repository: GetDataByIdRepo<Uri>) : GetItemByIdUseCase<Uri> {

    override fun get(id: Long): Flow<Uri?> = repository.getById(id)

}