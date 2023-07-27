package com.example.playlistmaker.domain.repositories

import com.example.playlistmaker.domain.entities.Track

interface SearchRepository {
    fun search(term: String): List<Track>?
}