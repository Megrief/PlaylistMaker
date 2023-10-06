package com.example.playlistmaker.data.search.network.network_client

import com.example.playlistmaker.data.search.dto.TrackSearchRequest
import com.example.playlistmaker.data.search.network.Response

interface NetworkClient {
    suspend fun doSearch(dto: TrackSearchRequest): Response
}