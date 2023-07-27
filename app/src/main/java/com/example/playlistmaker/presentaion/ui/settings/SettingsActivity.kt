package com.example.playlistmaker.presentaion.ui.settings

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.playlistmaker.app.App
import com.example.playlistmaker.R
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.switchmaterial.SwitchMaterial

class SettingsActivity : AppCompatActivity() {
    private val toolbar by lazy { findViewById<MaterialToolbar>(R.id.toolbar) }
    private val themeSwitcher by lazy { findViewById<SwitchMaterial>(R.id.dark_theme) }
    private val shareButton by lazy { findViewById<TextView>(R.id.share) }
    private val supportButton by lazy { findViewById<TextView>(R.id.support) }
    private val userAgreementButton by lazy { findViewById<TextView>(R.id.user_agreement) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        toolbar.setNavigationOnClickListener { onBackPressedDispatcher.onBackPressed() }
        configureShareButton()
        configureUserAgreementButton()
        configureSupportButton()
        configureThemeSwitcher()
    }

    private fun configureSupportButton() {
        supportButton.setOnClickListener {
            startActivity(Intent(Intent.ACTION_SENDTO).apply {
                this.data = Uri.parse("mailto:")
                this.putExtra(Intent.EXTRA_EMAIL, getString(R.string.supp_message_address))
                this.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.supp_message_theme))
                this.putExtra(Intent.EXTRA_TEXT, getString(R.string.supp_message_content))
            })
        }
    }

    private fun configureShareButton() {
        shareButton.setOnClickListener {
            startActivity(Intent(Intent.ACTION_SEND).apply {
                this.type = "text/plain"
                this.putExtra(Intent.EXTRA_TEXT, getString(R.string.android_developer))
            })
        }
    }

    private fun configureUserAgreementButton() {
        userAgreementButton.setOnClickListener {
            val url = Uri.parse(getString(R.string.practicum_offer))
            startActivity(Intent(Intent.ACTION_VIEW, url))
        }
    }

    private fun configureThemeSwitcher() {
        val app = application as App
        themeSwitcher.isChecked = app.themeCode == 2
        themeSwitcher.setOnCheckedChangeListener { _, checked ->
            app.switchTheme(checked)
        }
    }
}