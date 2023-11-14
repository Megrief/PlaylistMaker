package com.example.playlistmaker.data.sharing.external_navigator

import com.example.playlistmaker.domain.sharing.entities.EmailData

interface ExternalNavigator {

    fun shareApp(content: String)

    fun openUserAgreement(link: String)

    fun mailToSupport(emailData: EmailData)
}