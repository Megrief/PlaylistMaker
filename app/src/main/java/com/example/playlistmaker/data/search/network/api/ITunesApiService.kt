package com.example.playlistmaker.data.search.network.api

import retrofit2.http.GET
import retrofit2.http.Query

interface ITunesApiService {
    @GET("/search?entity=song")
    suspend fun search(@Query("term") text: String): ITunesResponse

}