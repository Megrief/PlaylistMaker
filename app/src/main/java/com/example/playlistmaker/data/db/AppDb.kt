package com.example.playlistmaker.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.playlistmaker.data.db.dto.TrackDb

@Database(
    version = 1,
    entities = [
        TrackDb::class
    ]
)
abstract class AppDb : RoomDatabase() {
    abstract fun getDbDao(): DbDao
}