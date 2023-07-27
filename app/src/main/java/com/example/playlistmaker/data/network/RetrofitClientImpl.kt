package com.example.playlistmaker.data.network

import com.example.playlistmaker.data.dto.TrackSearchRequest
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