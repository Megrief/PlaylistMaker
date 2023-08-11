package com.example.playlistmaker.domain.sharing

interface SharingRepository {
    fun shareApp()

    fun openUserAgreement()

    fun mailToSupport()
}