package com.example.playlistmaker.data.db.repo_impl

import com.example.playlistmaker.data.db.DbDao
import com.example.playlistmaker.domain.db.DbRepo
import com.example.playlistmaker.domain.entity.Track
import com.example.playlistmaker.utils.TrackConverter.toTrack
import com.example.playlistmaker.utils.TrackConverter.toTrackDb
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class DbRepoImpl(
    private val dbDao: DbDao,
) : DbRepo<Track> {

    override fun delete(id: Long) {
        val trackDb = dbDao.getById(id)
        if (trackDb != null) dbDao.delete(trackDb)
    }

    override fun getById(id: Long): Flow<Track?> {
        val trackDb = dbDao.getById(id)
        return flow { emit(trackDb?.toTrack()) }
    }

    override fun store(item: Track) {
        val trackDb = item.toTrackDb()
        dbDao.store(trackDb)
    }

    override fun get(): Flow<Track> {
        return flow {
            dbDao.getAll().forEach { trackDb ->
                emit(trackDb.toTrack())
            }
        }
    }


}