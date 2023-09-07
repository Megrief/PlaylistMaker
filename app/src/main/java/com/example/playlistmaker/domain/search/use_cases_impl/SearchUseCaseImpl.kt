package com.example.playlistmaker.domain.search.use_cases_impl

import com.example.playlistmaker.domain.entity.Track
import com.example.playlistmaker.domain.search.SearchRepository
import com.example.playlistmaker.domain.storage.use_cases.GetDataUseCase
import java.util.concurrent.Executors
import java.util.function.Consumer

class SearchUseCaseImpl(private val repository: SearchRepository) : GetDataUseCase<List<Track>?> {
    private val executor = Executors.newCachedThreadPool()

    override fun get(key: String, consumer: Consumer<List<Track>?>) {
        executor.execute {
            consumer.accept(
                repository.search(key)
            )
        }
    }
}