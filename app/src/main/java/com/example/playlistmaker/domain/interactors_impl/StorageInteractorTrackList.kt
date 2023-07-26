package com.example.playlistmaker.domain.interactors_impl

import com.example.playlistmaker.domain.entities.Track
import com.example.playlistmaker.domain.interactors.StorageInteractor
import com.example.playlistmaker.domain.repositories.StorageManagerRepo
import java.util.concurrent.Executors
import java.util.function.Consumer

class StorageInteractorTrackList(
    private val repository: StorageManagerRepo<List<Track>>
) : StorageInteractor<List<Track>> {
    private val executor = Executors.newCachedThreadPool()

    override fun save(key: String, item: List<Track>) {
        executor.execute {
            repository.save(key, item)
        }
    }

    override fun get(key: String, consumer: Consumer<List<Track>>) {
        executor.execute {
            consumer.accept(repository.get(key))
        }
    }

}