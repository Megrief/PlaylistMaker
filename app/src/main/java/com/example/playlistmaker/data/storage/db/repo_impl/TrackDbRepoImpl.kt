package com.example.playlistmaker.data.storage.db.repo_impl

import com.example.playlistmaker.data.storage.db.dao.TrackDbDao
import com.example.playlistmaker.domain.storage.db.DbRepo
import com.example.playlistmaker.domain.entities.Track
import com.example.playlistmaker.data.util.EntityConverter.toTrack
import com.example.playlistmaker.data.util.EntityConverter.toTrackDb
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class TrackDbRepoImpl(
    private val trackDbDao: TrackDbDao,
) : DbRepo<Track> {

    override fun delete(id: Long) {
        val trackDb = trackDbDao.getById(id)
        if (trackDb != null) trackDbDao.delete(trackDb)
    }

    override fun getById(id: Long): Flow<Track?> = flow {
        val trackDb = trackDbDao.getById(id)
        emit(trackDb?.toTrack())
    }

    override fun store(item: Track) {
        val trackDb = item.toTrackDb()
        trackDbDao.store(trackDb)
    }

    override fun get(): Flow<Track?> = flow {
        trackDbDao.getAll().forEach { trackDb ->
            emit(trackDb.toTrack())
        }
    }

}