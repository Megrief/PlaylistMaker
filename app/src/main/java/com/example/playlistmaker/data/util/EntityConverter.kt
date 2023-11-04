package com.example.playlistmaker.data.util

import com.example.playlistmaker.data.storage.db.dto.TrackDb
import com.example.playlistmaker.data.search.dto.TrackDto
import com.example.playlistmaker.data.storage.db.dto.PlaylistDb
import com.example.playlistmaker.data.storage.db.dto.TrackIdsDto
import com.example.playlistmaker.domain.entities.Track
import com.example.playlistmaker.domain.entities.Playlist
import com.google.gson.Gson
import org.koin.java.KoinJavaComponent.getKoin
import java.io.File
import java.time.LocalDateTime

object EntityConverter {

    private val gson: Gson = getKoin().get()

    fun Track.toTrackDb(): TrackDb =
        TrackDb(
            trackId = id,
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
            id = trackId,
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
            id = trackId ?: -1,
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

    fun Playlist.toPlaylistDb(): PlaylistDb =
        PlaylistDb(
            id = id,
            name = name,
            description = description,
            photoPath = photoFile?.path ?: "",
            trackIdsList = trackIdsList.trackIdsListToJson(),
            addingDate = System.currentTimeMillis()
        )

    fun PlaylistDb.toPlaylist(): Playlist =
        Playlist(
            id = id,
            name = name,
            description = description,
            photoFile = getPhotoFile(photoPath),
            trackIdsList = trackIdsList.fromJsonToTrackIdsList()
        )

    private fun getPhotoFile(name: String): File? = if(name.isBlank()) null else File(name)

    private fun MutableList<Long>.trackIdsListToJson(): String =
        gson.toJson(TrackIdsDto(this))

    private fun String.fromJsonToTrackIdsList(): MutableList<Long> =
        gson.fromJson(this, TrackIdsDto::class.java).ids

    private fun getYear(dateString: String) =
        LocalDateTime.parse(dateString.dropLast(1)).year.toString()

}