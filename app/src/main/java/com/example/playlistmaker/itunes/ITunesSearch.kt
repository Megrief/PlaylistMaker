package com.example.playlistmaker.itunes

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ITunesSearch {
    @GET("/search?entity=song")
    fun search(@Query("term") text: String): Call<ITunesResponse>

}