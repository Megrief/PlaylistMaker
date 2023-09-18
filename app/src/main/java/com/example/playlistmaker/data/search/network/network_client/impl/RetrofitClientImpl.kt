package com.example.playlistmaker.data.search.network.network_client.impl

import com.example.playlistmaker.data.search.dto.TrackSearchRequest
import com.example.playlistmaker.data.search.network.Response
import com.example.playlistmaker.data.search.network.api.ITunesApiService
import com.example.playlistmaker.data.search.network.network_client.NetworkClient
import java.io.IOException

class RetrofitClientImpl(private val apiService: ITunesApiService) : NetworkClient {

    override fun doSearch(dto: TrackSearchRequest): Response {
        val response = try {
            apiService.search(dto.term).execute()
        } catch(_: IOException) {
            null
        }
        val body = response?.body() ?: Response()

        body.apply { resultCode = response?.code() ?: Response.FAILURE }

        return body
    }
}