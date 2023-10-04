package com.example.playlistmaker.data.search

import com.example.playlistmaker.data.search.dto.TrackSearchRequest
import com.example.playlistmaker.data.search.network.Response
import com.example.playlistmaker.data.search.network.api.ITunesResponse
import com.example.playlistmaker.data.search.network.network_client.NetworkClient
import com.example.playlistmaker.domain.entity.Track
import com.example.playlistmaker.domain.search.SearchRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.time.LocalDateTime

class SearchRepoImpl(private val networkClient: NetworkClient) : SearchRepository {
    override suspend fun search(term: String): Flow<List<Track>?> {
        val response = networkClient.doSearch(TrackSearchRequest(term))

        return flow {
            if (response.resultCode == Response.SUCCESS) {
                response as ITunesResponse
                response.results.map { track ->
                    with(track) {
                        Track(
                            trackId = trackId ?: -1,
                            trackName = trackName ?: "",
                            trackTime = trackTime ?: 0L,
                            artworkUrl100 = artworkUrl100 ?: "",
                            artistName = artistName ?: "",
                            collectionName = track.collectionName ?: "",
                            country = country ?: "",
                            primaryGenreName = primaryGenreName ?: "",
                            releaseDate = releaseDate?.let { date -> getYear(date) } ?: "",
                            previewUrl = previewUrl ?: ""
                        )
                    }

                }.let { emit(it) }
            } else emit(null)
        }
    }

    private fun getYear(dateString: String) = LocalDateTime.parse(dateString.dropLast(1)).year.toString()

}