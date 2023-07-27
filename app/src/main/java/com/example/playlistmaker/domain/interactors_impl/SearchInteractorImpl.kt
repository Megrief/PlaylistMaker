package com.example.playlistmaker.domain.interactors_impl

import com.example.playlistmaker.domain.entities.Track
import com.example.playlistmaker.domain.interactors.SearchInteractor
import com.example.playlistmaker.domain.repositories.SearchRepository
import java.util.concurrent.Executors
import java.util.function.Consumer

class SearchInteractorImpl(private val repository: SearchRepository) : SearchInteractor {
    private val executor = Executors.newCachedThreadPool()

    override fun search(term: String, consumer: Consumer<List<Track>?>) {
        executor.execute {
            consumer.accept(repository.search(term))
        }
    }
}