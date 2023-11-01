package com.example.playlistmaker.data.storage.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.playlistmaker.data.storage.db.dao.PlaylistDbDao
import com.example.playlistmaker.data.storage.db.dao.TrackDbDao
import com.example.playlistmaker.data.storage.db.dto.PlaylistDb
import com.example.playlistmaker.data.storage.db.dto.TrackDb

@Database(
    version = 2,
    entities = [
        TrackDb::class,
        PlaylistDb::class
    ],
    exportSchema = false
)
abstract class AppDb : RoomDatabase() {
    abstract val trackDbDao: TrackDbDao
    abstract val playlistDbDao: PlaylistDbDao
}