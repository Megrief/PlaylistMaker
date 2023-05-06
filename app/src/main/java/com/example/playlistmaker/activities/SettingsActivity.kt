package com.example.playlistmaker.activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import com.example.playlistmaker.R
import com.google.android.material.appbar.MaterialToolbar

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)


        val toolbar = findViewById<MaterialToolbar>(R.id.toolbar)
        val darkThemeSwitch = findViewById<SwitchCompat>(R.id.dark_theme)
        val shareButton = findViewById<TextView>(R.id.share)
        val supportButton = findViewById<TextView>(R.id.support)
        val userAgreementButton = findViewById<TextView>(R.id.user_agreement)

        toolbar.setNavigationOnClickListener {
            finish()
        }

        shareButton.setOnClickListener {
            startActivity(Intent(Intent.ACTION_SEND).apply {
                this.type = "text/plain"
                this.putExtra(Intent.EXTRA_TEXT, getString(R.string.android_developer))
            })
        }

        supportButton.setOnClickListener {
            startActivity(Intent(Intent.ACTION_SENDTO).apply {
                this.data = Uri.parse("mailto:")
                this.putExtra(Intent.EXTRA_EMAIL, getString(R.string.supp_message_address))
                this.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.supp_message_theme))
                this.putExtra(Intent.EXTRA_TEXT, getString(R.string.supp_message_content))
            })
        }

        userAgreementButton.setOnClickListener {
            val url = Uri.parse(getString(R.string.practicum_offer))
            startActivity(Intent(Intent.ACTION_VIEW, url))
        }

    }
}