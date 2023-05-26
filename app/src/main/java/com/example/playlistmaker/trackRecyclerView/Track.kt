package com.example.playlistmaker.trackRecyclerView

import com.google.gson.annotations.SerializedName

data class Track(
    @SerializedName("trackName")
    val trackName: String,
    @SerializedName("artistName")
    val artistName: String,
    @SerializedName("trackTimeMillis")
    val trackTime: Long,
    @SerializedName("artworkUrl100")
    val artworkUrl100: String
) {
    override fun toString(): String {
        return "$trackName\n$artistName"
    }
}