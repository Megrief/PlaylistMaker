package com.example.playlistmaker.data.network


import com.example.playlistmaker.data.dto.TrackDto
import com.google.gson.annotations.SerializedName

data class ITunesResponse(
    @SerializedName("resultCount")
    val resultCount: Int = 0,
    @SerializedName("results")
    val results: List<TrackDto> = emptyList()
) : Response()