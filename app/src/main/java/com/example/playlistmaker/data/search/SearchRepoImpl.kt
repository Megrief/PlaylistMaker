package com.example.playlistmaker.data.search

import com.example.playlistmaker.data.search.dto.TrackSearchRequest
import com.example.playlistmaker.data.search.network.Response
import com.example.playlistmaker.data.search.network.api.ITunesResponse
import com.example.playlistmaker.data.search.network.network_client.NetworkClient
import com.example.playlistmaker.domain.entity.Track
import com.example.playlistmaker.domain.search.SearchRepository
import com.example.playlistmaker.data.util.EntityConverter.toTrack
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class SearchRepoImpl(private val networkClient: NetworkClient) : SearchRepository {
    override suspend fun search(term: String): Flow<List<Track>?> {
        val response = networkClient.doSearch(TrackSearchRequest(term))

        return flow {
            emit(
                if (response.resultCode == Response.SUCCESS)
                    (response as ITunesResponse).results.map { it.toTrack() }
                else null
            )
        }
    }


}