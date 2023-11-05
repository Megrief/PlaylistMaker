package com.example.playlistmaker.data.storage.db.repo_impl

import com.example.playlistmaker.data.storage.db.dao.TrackInPlaylistsDao
import com.example.playlistmaker.data.util.EntityConverter.toTrack
import com.example.playlistmaker.data.util.EntityConverter.toTrackInPlaylist
import com.example.playlistmaker.domain.entities.Track
import com.example.playlistmaker.domain.storage.db.DbRepo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class TrackInPlaylistDbRepoImpl(
    private val trackInPlaylistDbDao: TrackInPlaylistsDao
) : DbRepo<Track> {

    override fun delete(id: Long) {
        val trackDb = this.trackInPlaylistDbDao.getById(id)
        if (trackDb != null) this.trackInPlaylistDbDao.delete(trackDb)
    }

    override fun getById(id: Long): Flow<Track?> = flow {
        val trackDb = this@TrackInPlaylistDbRepoImpl.trackInPlaylistDbDao.getById(id)
        emit(trackDb?.toTrack())
    }

    override fun store(item: Track) {
        val trackDb = item.toTrackInPlaylist()
        this.trackInPlaylistDbDao.store(trackDb)
    }

    override fun get(): Flow<Track?> = flow {
        emit(null)
    }
}