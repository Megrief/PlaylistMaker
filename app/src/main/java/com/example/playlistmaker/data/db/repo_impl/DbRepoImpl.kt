package com.example.playlistmaker.data.db.repo_impl

import com.example.playlistmaker.data.db.AppDb
import com.example.playlistmaker.data.db.util.TrackConverter
import com.example.playlistmaker.domain.db.DbRepo
import com.example.playlistmaker.domain.entity.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class DbRepoImpl(
    appDb: AppDb,
) : DbRepo<Track> {
    private val dbDao = appDb.getDbDao()

    override fun delete(id: Long) {
        val trackDb = dbDao.getById(id)
        if (trackDb != null) dbDao.delete(trackDb)
    }

    override fun getById(id: Long): Flow<Track?> {
        val trackDb = dbDao.getById(id)
        return flow {
            emit(
                if (trackDb == null) null
                else TrackConverter.convertToTrack(trackDb)
            )
        }
    }

    override fun store(item: Track) {
        val trackDb = TrackConverter.convertToTrackDb(item)
        dbDao.store(trackDb)
    }

    override fun get(): Flow<Track> {
        return flow {
            dbDao.getAll().forEach { trackDb ->
                emit(TrackConverter.convertToTrack(trackDb))
            }
        }
    }


}