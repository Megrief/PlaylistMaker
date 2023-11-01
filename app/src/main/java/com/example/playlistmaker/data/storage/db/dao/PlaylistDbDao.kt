package com.example.playlistmaker.data.storage.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.playlistmaker.data.storage.db.dto.PlaylistDb

@Dao
interface PlaylistDbDao {

    @Insert(entity = PlaylistDb::class, onConflict = OnConflictStrategy.REPLACE)
    fun store(playlistDb: PlaylistDb)

    @Delete(entity = PlaylistDb::class)
    fun delete(playlistDb: PlaylistDb)

    @Query("SELECT * FROM playlists_table ORDER BY addingDate DESC")
    fun getAll(): List<PlaylistDb>

    @Query("SELECT * FROM playlists_table WHERE id = :id")
    fun getById(id: Long): PlaylistDb?
}