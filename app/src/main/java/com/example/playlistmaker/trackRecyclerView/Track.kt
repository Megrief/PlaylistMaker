package com.example.playlistmaker.trackRecyclerView

import com.google.gson.annotations.SerializedName

data class Track(
    @SerializedName("trackId")
    val trackId: Long,
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

    override fun equals(other: Any?): Boolean {
        return if (other is Track) this.trackId == other.trackId else false
    }

    override fun hashCode(): Int {
        return super.hashCode()
    }
}