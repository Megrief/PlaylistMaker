package com.example.playlistmaker.data.storage.db.repo_impl

import com.example.playlistmaker.data.storage.db.dao.TrackDbDao
import com.example.playlistmaker.data.util.EntityConverter.toTrack
import com.example.playlistmaker.data.util.EntityConverter.toTrackDb
import com.example.playlistmaker.domain.entities.Track
import com.example.playlistmaker.domain.storage.repos.DbManagerRepoData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class TrackDbRepoImplData(
    private val trackDbDao: TrackDbDao,
) : DbManagerRepoData<Track> {

    override fun delete(item: Track) {
        trackDbDao.delete(item.toTrackDb())
    }

    override fun getById(id: Long): Flow<Track?> = flow {
        val trackDb = trackDbDao.getById(id)
        emit(trackDb?.toTrack())
    }

    override fun store(item: Track): Boolean {
        val trackDb = item.toTrackDb()
        trackDbDao.store(trackDb)
        return true
    }

    override fun get(): Flow<Track?> = flow {
        trackDbDao.getAll().forEach { trackDb ->
            emit(trackDb.toTrack())
        }
    }

}