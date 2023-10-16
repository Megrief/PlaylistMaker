package com.example.playlistmaker.data.db.util

import com.example.playlistmaker.data.db.dto.TrackDb
import com.example.playlistmaker.domain.entity.Track
import java.util.Date

class TrackConverter(private val date: Date) {

    fun convertToTrackDb(track: Track): TrackDb =
        TrackDb(
            trackId = track.trackId,
            trackName = track.trackName,
            artistName = track.artistName,
            trackTime = track.trackTime,
            artworkUrl100 = track.artworkUrl100,
            collectionName = track.collectionName,
            country = track.country,
            releaseDate = track.releaseDate,
            primaryGenreName = track.primaryGenreName,
            previewUrl = track.previewUrl,
            addingDate = date.time
        )

    fun convertToTrack(trackDb: TrackDb): Track = Track(
        trackId = trackDb.trackId,
        trackName = trackDb.trackName,
        artistName = trackDb.artistName,
        trackTime = trackDb.trackTime,
        artworkUrl100 = trackDb.artworkUrl100,
        collectionName = trackDb.collectionName,
        country = trackDb.country,
        releaseDate = trackDb.releaseDate,
        primaryGenreName = trackDb.primaryGenreName,
        previewUrl = trackDb.previewUrl
    )
}