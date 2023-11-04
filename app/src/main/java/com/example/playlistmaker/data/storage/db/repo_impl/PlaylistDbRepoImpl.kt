package com.example.playlistmaker.data.storage.db.repo_impl

import com.example.playlistmaker.data.storage.db.dao.PlaylistDbDao
import com.example.playlistmaker.domain.entities.Playlist
import com.example.playlistmaker.domain.storage.db.DbRepo
import com.example.playlistmaker.data.util.EntityConverter.toPlaylist
import com.example.playlistmaker.data.util.EntityConverter.toPlaylistDb
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class PlaylistDbRepoImpl(private val playlistDbDao: PlaylistDbDao) : DbRepo<Playlist> {

    override fun store(item: Playlist) {
        val playlistDb = item.toPlaylistDb()
        playlistDbDao.store(playlistDb)
    }

    override fun delete(id: Long) {
        val playlistDb = playlistDbDao.getById(id)
        if (playlistDb != null) playlistDbDao.delete(playlistDb)
    }

    override fun getById(id: Long): Flow<Playlist?>  = flow {
        val playlistDb = playlistDbDao.getById(id)
        emit(playlistDb?.toPlaylist())
    }

    override fun get(): Flow<Playlist?> = flow {
        playlistDbDao.getAll().forEach {
            emit(it.toPlaylist())
        }
    }

}