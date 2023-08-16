package com.example.playlistmaker.data.search.network.network_client.impl

import com.example.playlistmaker.data.search.dto.TrackSearchRequest
import com.example.playlistmaker.data.search.network.Response
import com.example.playlistmaker.data.search.network.api.ITunesApiService
import com.example.playlistmaker.data.search.network.network_client.NetworkClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.SocketTimeoutException

class RetrofitClientImpl : NetworkClient {
    private val baseUrl = "https://itunes.apple.com"
    private val retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    private val apiService = retrofit.create(ITunesApiService::class.java)

    override fun doSearch(dto: TrackSearchRequest): Response {
        val response = try {
            apiService.search(dto.term).execute()
        } catch(_: SocketTimeoutException) {
            null
        }
        val body = response?.body() ?: Response()

        body.apply { resultCode = response?.code() ?: Response.FAILURE }

        return body
    }
}