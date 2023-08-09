package com.example.playlistmaker.data.search.network.api


import com.example.playlistmaker.data.search.dto.TrackDto
import com.example.playlistmaker.data.search.network.Response
import com.google.gson.annotations.SerializedName

data class ITunesResponse(
    @SerializedName("resultCount")
    val resultCount: Int = 0,
    @SerializedName("results")
    val results: List<TrackDto> = emptyList()
) : Response()