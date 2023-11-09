package com.example.playlistmaker.data.storage.db.repo_impl

import com.example.playlistmaker.data.storage.db.dao.PlaylistDbDao
import com.example.playlistmaker.data.util.EntityConverter.toPlaylist
import com.example.playlistmaker.data.util.EntityConverter.toPlaylistDb
import com.example.playlistmaker.domain.entities.Playlist
import com.example.playlistmaker.domain.storage.repos.DbManagerRepoData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class PlaylistDbRepoImplData(private val playlistDbDao: PlaylistDbDao) : DbManagerRepoData<Playlist> {

    override fun store(item: Playlist): Boolean {
        val playlistDb = item.toPlaylistDb()
        playlistDbDao.store(playlistDb)
        return true
    }

    override fun delete(item: Playlist) {
        playlistDbDao.delete(item.toPlaylistDb())
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