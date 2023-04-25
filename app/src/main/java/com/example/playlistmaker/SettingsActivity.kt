package com.example.playlistmaker

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val backButton = findViewById<ImageButton>(R.id.back)

        backButton.setOnClickListener {
            finish()
        }

        // TODO
        val darkThemeSwitch = findViewById<SwitchCompat>(R.id.dark_theme)

        val shareButton = findViewById<TextView>(R.id.share)
        shareButton.setOnClickListener {
            startActivity(Intent(Intent.ACTION_SEND).apply {
                this.type = "text/plain"
                this.putExtra(Intent.EXTRA_TEXT, "https://practicum.yandex.ru/profile/android-developer/")
            })
        }

        val supportButton = findViewById<TextView>(R.id.support)
        supportButton.setOnClickListener {
            startActivity(Intent(Intent.ACTION_SENDTO).apply {
                this.data = Uri.parse("mailto:")
                this.putExtra(Intent.EXTRA_EMAIL, "eu.pim@mail.ru")
                this.putExtra(Intent.EXTRA_SUBJECT, "Сообщение разработчикам и разработчицам приложения Playlist Maker")
                this.putExtra(Intent.EXTRA_TEXT, "Спасибо разработчикам и разработчицам за крутое приложение!")
            })
        }

        val userAgreementButton = findViewById<TextView>(R.id.user_agreement)
        userAgreementButton.setOnClickListener {
            val url = Uri.parse("https://yandex.ru/legal/practicum_offer/")
            startActivity(Intent(Intent.ACTION_VIEW, url))
        }

    }
}