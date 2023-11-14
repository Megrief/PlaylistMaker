package com.example.playlistmaker.domain.playlist_page

import com.example.playlistmaker.domain.sharing.SharingRepository
import com.example.playlistmaker.domain.sharing.use_cases.ShareStringUseCase

class SharePlaylistUseCase(private val repository: SharingRepository) : ShareStringUseCase {
    override fun share(content: String) {
        repository.share(content)
    }
}