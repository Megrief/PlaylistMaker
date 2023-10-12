package com.example.playlistmaker.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.playlistmaker.data.db.dto.TrackDb

@Dao
interface DbDao {

    @Insert(entity = TrackDb::class, onConflict = OnConflictStrategy.REPLACE)
    fun store(trackDb: TrackDb)

    @Delete(entity = TrackDb::class)
    fun delete(trackDb: TrackDb)

    @Query("SELECT * FROM favourite_tracks_table")
    fun getAll(): List<TrackDb>

    @Query("SELECT * FROM favourite_tracks_table WHERE trackId = :id")
    fun getById(id: Long): TrackDb?
}