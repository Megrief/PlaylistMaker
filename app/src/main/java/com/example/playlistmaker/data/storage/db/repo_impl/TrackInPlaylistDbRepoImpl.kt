package com.example.playlistmaker.data.storage.db.repo_impl

import com.example.playlistmaker.data.storage.db.dao.TrackInPlaylistsDao
import com.example.playlistmaker.data.util.EntityConverter.toTrack
import com.example.playlistmaker.data.util.EntityConverter.toTrackInPlaylist
import com.example.playlistmaker.domain.entities.Track
import com.example.playlistmaker.domain.storage.repos.DbManagerRepo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class TrackInPlaylistDbRepoImpl(
    private val trackInPlaylistDbDao: TrackInPlaylistsDao
) : DbManagerRepo<Track> {

    override fun delete(item: Track) {
        trackInPlaylistDbDao.delete(item.toTrackInPlaylist())
    }

    override fun getById(id: Long): Flow<Track?> = flow {
        val trackDb = this@TrackInPlaylistDbRepoImpl.trackInPlaylistDbDao.getById(id)
        emit(trackDb?.toTrack())
    }

    override fun store(item: Track): Boolean {
        val trackDb = item.toTrackInPlaylist()
        trackInPlaylistDbDao.store(trackDb)
        return true
    }

    override fun get(): Flow<Track?> = flow {
        emit(null)
    }
}