package com.example.playlistmaker.domain.sharing

interface SharingRepository {
    fun share(content: String?)

    fun openUserAgreement()

    fun mailToSupport()
}