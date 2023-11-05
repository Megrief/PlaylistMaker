package com.example.playlistmaker.data.storage.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.playlistmaker.data.storage.db.dto.TrackInPlaylistDb


@Dao
interface TrackInPlaylistsDao {

    @Insert(entity = TrackInPlaylistDb::class, onConflict = OnConflictStrategy.IGNORE)
    fun store(trackDb: TrackInPlaylistDb)

    @Query("SELECT * FROM track_in_playlists_table WHERE trackId = :id")
    fun getById(id: Long): TrackInPlaylistDb?

    @Delete(entity = TrackInPlaylistDb::class)
    fun  delete(trackInPlaylistDb: TrackInPlaylistDb)
}