package com.example.playlistmaker.domain.interactors

import com.example.playlistmaker.domain.entities.Track
import java.util.function.Consumer

interface SearchInteractor {
    fun search(term: String, consumer: Consumer<List<Track>?>)
}