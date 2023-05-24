package com.example.playlistmaker.itunes


import com.example.playlistmaker.trackRecyclerView.Track
import com.google.gson.annotations.SerializedName

data class ITunesResponse(
    @SerializedName("resultCount")
    val resultCount: Int = 0,
    @SerializedName("results")
    val results: List<Track> = emptyList()
)