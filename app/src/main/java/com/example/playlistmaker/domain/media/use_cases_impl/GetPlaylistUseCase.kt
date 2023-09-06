package com.example.playlistmaker.domain.media.use_cases_impl

import com.example.playlistmaker.domain.media.entity.Playlist
import com.example.playlistmaker.domain.storage.StorageManagerRepo
import com.example.playlistmaker.domain.storage.use_cases.GetDataUseCase
import java.util.function.Consumer

class GetPlaylistUseCase(
    private val repository: StorageManagerRepo<Playlist?>
) : GetDataUseCase<Playlist?> {

    override fun get(key: String, consumer: Consumer<Playlist?>) {
        consumer.accept(repository.get(key))
    }
}