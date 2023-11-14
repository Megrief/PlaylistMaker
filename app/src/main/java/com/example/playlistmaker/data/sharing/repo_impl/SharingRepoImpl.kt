package com.example.playlistmaker.data.sharing.repo_impl

import android.content.res.Resources
import com.example.playlistmaker.R
import com.example.playlistmaker.data.sharing.external_navigator.ExternalNavigator
import com.example.playlistmaker.domain.sharing.SharingRepository
import com.example.playlistmaker.domain.sharing.entities.EmailData

class SharingRepoImpl(
    private val resources: Resources,
    private val externalNavigator: ExternalNavigator
) : SharingRepository {

    override fun share(content: String?) {
        externalNavigator.shareApp(content ?: getShareLink())
    }

    override fun openUserAgreement() {
        externalNavigator.openUserAgreement(getUserAgreementLink())
    }

    override fun mailToSupport() {
        externalNavigator.mailToSupport(getEmailData())
    }

    private fun getShareLink(): String {
        return resources.getString(R.string.android_developer)
    }

    private fun getUserAgreementLink(): String {
        return resources.getString(R.string.practicum_offer)
    }

    private fun getEmailData(): EmailData {
        with(resources) {
            return EmailData(
                getString(R.string.supp_message_address),
                getString(R.string.supp_message_theme),
                getString(R.string.supp_message_content)
            )
        }
    }
}