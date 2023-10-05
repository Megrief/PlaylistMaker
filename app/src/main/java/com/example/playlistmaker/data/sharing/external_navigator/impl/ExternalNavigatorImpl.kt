package com.example.playlistmaker.data.sharing.external_navigator.impl

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.example.playlistmaker.data.sharing.external_navigator.ExternalNavigator
import com.example.playlistmaker.domain.sharing.entities.EmailData

class ExternalNavigatorImpl(private val context: Context) : ExternalNavigator {

    override fun shareApp(link: String) {
        Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, link)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }.also { context.startActivity(it) }
    }

    override fun openUserAgreement(link: String) {
        Intent(Intent.ACTION_VIEW, Uri.parse(link))
            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            .also { context.startActivity(it) }
    }

    override fun mailToSupport(emailData: EmailData) {
        Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:")
            putExtra(Intent.EXTRA_EMAIL, emailData.email)
            putExtra(Intent.EXTRA_SUBJECT, emailData.subject)
            putExtra(Intent.EXTRA_TEXT, emailData.text)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }.also { context.startActivity(it) }
    }
}