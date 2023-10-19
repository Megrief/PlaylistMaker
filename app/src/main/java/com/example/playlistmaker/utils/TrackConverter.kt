package com.example.playlistmaker.utils

import com.example.playlistmaker.data.db.dto.TrackDb
import com.example.playlistmaker.data.search.dto.TrackDto
import com.example.playlistmaker.domain.entity.Track
import java.time.LocalDateTime

object TrackConverter {
    fun Track.toTrackDb(): TrackDb =
        TrackDb(
            trackId = trackId,
            trackName = trackName,
            artistName = artistName,
            trackTime = trackTime,
            artworkUrl100 = artworkUrl100,
            collectionName = collectionName,
            country = country,
            releaseDate = releaseDate,
            primaryGenreName = primaryGenreName,
            previewUrl = previewUrl,
            addingDate = System.currentTimeMillis()
        )

    fun TrackDb.toTrack(): Track =
        Track(
            trackId = trackId,
            trackName = trackName,
            artistName = artistName,
            trackTime = trackTime,
            artworkUrl100 = artworkUrl100,
            collectionName = collectionName,
            country = country,
            releaseDate = releaseDate,
            primaryGenreName = primaryGenreName,
            previewUrl = previewUrl
        )

    fun TrackDto.toTrack(): Track =
        Track(
            trackId = trackId ?: -1,
            trackName = trackName ?: "",
            trackTime = trackTime ?: 0L,
            artworkUrl100 = artworkUrl100 ?: "",
            artistName = artistName ?: "",
            collectionName = collectionName ?: "",
            country = country ?: "",
            primaryGenreName = primaryGenreName ?: "",
            releaseDate = releaseDate?.let { date -> getYear(date) } ?: "",
            previewUrl = previewUrl ?: ""
        )

    private fun getYear(dateString: String) = LocalDateTime.parse(dateString.dropLast(1)).year.toString()

}