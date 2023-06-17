package com.example.playlistmaker.trackRecyclerView

import android.icu.text.SimpleDateFormat
import com.google.gson.annotations.SerializedName
import java.time.LocalDateTime
import java.util.Locale

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
    val artworkUrl100: String,
    @SerializedName("collectionName")
    val collectionName: String,
    @SerializedName("country")
    val country: String,
    @SerializedName("releaseDate")
    val releaseDate: String,
    @SerializedName("primaryGenreName")
    val primaryGenreName: String
) {
    fun getYear() = LocalDateTime.parse(releaseDate.dropLast(1)).year.toString()
    fun getLength(): String = SimpleDateFormat("mm:ss", Locale.getDefault()).format(trackTime)
    fun getPoster512(): String = artworkUrl100.replaceAfterLast('/', "512x512bb.jpg")
}