package com.example.playlistmaker.domain.search

import com.example.playlistmaker.domain.entity.Track

interface SearchRepository {
    fun search(term: String): List<Track>?
}