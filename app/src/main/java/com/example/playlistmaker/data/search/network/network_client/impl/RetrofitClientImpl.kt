package com.example.playlistmaker.data.search.network.network_client.impl

import android.util.Log
import com.example.playlistmaker.data.search.dto.TrackSearchRequest
import com.example.playlistmaker.data.search.network.Response
import com.example.playlistmaker.data.search.network.api.ITunesApiService
import com.example.playlistmaker.data.search.network.network_client.NetworkClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException

class RetrofitClientImpl(private val apiService: ITunesApiService) : NetworkClient {

    override suspend fun doSearch(dto: TrackSearchRequest): Response {

        return withContext(Dispatchers.IO) {
            try {
                apiService.search(dto.term).apply { resultCode = Response.SUCCESS }
            } catch (_: IOException) {
                Response(Response.FAILURE)
            } catch (_: Exception) {
                Log.e("EXCEPTION", "Unknown exception")
                Response(Response.FAILURE)
            }
        }
    }
}