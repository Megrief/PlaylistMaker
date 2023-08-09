package com.example.playlistmaker.domain.storage.use_cases_impl

import com.example.playlistmaker.domain.entities.Track
import com.example.playlistmaker.domain.storage.StorageManagerRepo
import com.example.playlistmaker.domain.use_cases.GetDataUseCase
import java.util.function.Consumer

class GetTrackUseCase(private val repository: StorageManagerRepo<Track?>) : GetDataUseCase<Track?> {

    override fun get(key: String, consumer: Consumer<Track?>) {
        consumer.accept(repository.get(key))
    }
}