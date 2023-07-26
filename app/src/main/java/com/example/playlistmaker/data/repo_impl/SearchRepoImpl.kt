package com.example.playlistmaker.data.repo_impl

import android.icu.text.SimpleDateFormat
import com.example.playlistmaker.data.dto.TrackSearchRequest
import com.example.playlistmaker.data.network.ITunesResponse
import com.example.playlistmaker.data.network.NetworkClient
import com.example.playlistmaker.domain.entities.Track
import com.example.playlistmaker.domain.repositories.SearchRepository
import java.time.LocalDateTime
import java.util.Locale

class SearchRepoImpl(private val networkClient: NetworkClient) : SearchRepository {
    override fun search(term: String): List<Track>? {
        val response = networkClient.doRequest(TrackSearchRequest(term))

        return if (response.resultCode == 200) {
            response as ITunesResponse
            if (response.resultCount == 0) emptyList() else {
                response.results.map {
                    Track(
                        trackId = it.trackId ?: -1,
                        trackName = it.trackName ?: "",
                        trackTime = it.trackTime?.let { len -> getLength(len) } ?: "00:00",
                        artworkUrl100 = it.artworkUrl100 ?: "",
                        artistName = it.artistName ?: "",
                        collectionName = it.collectionName ?: "",
                        country = it.country ?: "",
                        primaryGenreName = it.primaryGenreName ?: "",
                        releaseDate = it.releaseDate?.let { date -> getYear(date) } ?: "",
                        previewUrl = it.previewUrl ?: ""
                    )
                }
            }
        } else null
    }

    private fun getYear(dateString: String) = LocalDateTime.parse(dateString.dropLast(1)).year.toString()

    private fun getLength(time: Long): String = SimpleDateFormat("mm:ss", Locale.getDefault()).format(time)
}