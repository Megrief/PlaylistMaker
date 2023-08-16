package com.example.playlistmaker.data.search

import com.example.playlistmaker.data.search.dto.TrackSearchRequest
import com.example.playlistmaker.data.search.network.Response
import com.example.playlistmaker.data.search.network.api.ITunesResponse
import com.example.playlistmaker.data.search.network.network_client.NetworkClient
import com.example.playlistmaker.domain.entities.Track
import com.example.playlistmaker.domain.search.SearchRepository
import java.time.LocalDateTime

class SearchRepoImpl(private val networkClient: NetworkClient) : SearchRepository {
    override fun search(term: String): List<Track>? {
        val response = networkClient.doSearch(TrackSearchRequest(term))
        return if (response.resultCode == Response.SUCCESS) {
            response as ITunesResponse
            if (response.resultCount == 0) emptyList() else {
                response.results.map {

                    Track(
                        trackId = it.trackId ?: -1,
                        trackName = it.trackName ?: "",
                        trackTime = it.trackTime ?: 0L,
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

}