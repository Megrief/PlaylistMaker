package com.example.playlistmaker.data.search.dto

import com.google.gson.annotations.SerializedName

data class TrackDto(
    @SerializedName("trackId")
    val trackId: Long?,
    @SerializedName("trackName")
    val trackName: String?,
    @SerializedName("artistName")
    val artistName: String?,
    @SerializedName("trackTimeMillis")
    val trackTime: Long?,
    @SerializedName("artworkUrl100")
    val artworkUrl100: String?,
    @SerializedName("collectionName")
    val collectionName: String?,
    @SerializedName("country")
    val country: String?,
    @SerializedName("releaseDate")
    val releaseDate: String?,
    @SerializedName("primaryGenreName")
    val primaryGenreName: String?,
    @SerializedName("previewUrl")
    val previewUrl: String?
)
