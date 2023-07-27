package com.example.playlistmaker.data.network

import com.example.playlistmaker.data.dto.TrackSearchRequest

interface NetworkClient {
    fun doSearch(dto: TrackSearchRequest): Response
}