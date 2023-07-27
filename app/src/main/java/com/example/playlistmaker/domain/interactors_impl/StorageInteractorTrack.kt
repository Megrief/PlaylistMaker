package com.example.playlistmaker.domain.interactors_impl

import com.example.playlistmaker.domain.entities.Track
import com.example.playlistmaker.domain.interactors.StorageInteractor
import com.example.playlistmaker.domain.repositories.StorageManagerRepo
import java.util.function.Consumer

class StorageInteractorTrack(
    private val repository: StorageManagerRepo<Track?>
) : StorageInteractor<Track?> {

    override fun save(key: String, item: Track?) {
        repository.save(key, item)
    }

    override fun get(key: String, consumer: Consumer<Track?>) {
        consumer.accept(repository.get(key))
    }

}