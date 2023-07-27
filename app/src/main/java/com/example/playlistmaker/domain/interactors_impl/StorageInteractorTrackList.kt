package com.example.playlistmaker.domain.interactors_impl

import com.example.playlistmaker.domain.entities.Track
import com.example.playlistmaker.domain.interactors.StorageInteractor
import com.example.playlistmaker.domain.repositories.StorageManagerRepo
import java.util.function.Consumer

class StorageInteractorTrackList(
    private val repository: StorageManagerRepo<List<Track>>
) : StorageInteractor<List<Track>> {

    override fun save(key: String, item: List<Track>) {
        repository.save(key, item)
    }

    override fun get(key: String, consumer: Consumer<List<Track>>) {
        consumer.accept(repository.get(key))
    }

}