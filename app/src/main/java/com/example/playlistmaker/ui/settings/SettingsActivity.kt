package com.example.playlistmaker.ui.settings

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import com.example.playlistmaker.app.App
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivitySettingsBinding

class SettingsActivity : AppCompatActivity() {
    private val binding by lazy { ActivitySettingsBinding.inflate(LayoutInflater.from(this)) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.toolbar.setNavigationOnClickListener { onBackPressedDispatcher.onBackPressed() }
        configureShareButton()
        configureUserAgreementButton()
        configureSupportButton()
        configureThemeSwitcher()
    }

    private fun configureSupportButton() {
        binding.support.setOnClickListener {
            startActivity(Intent(Intent.ACTION_SENDTO).apply {
                this.data = Uri.parse("mailto:")
                this.putExtra(Intent.EXTRA_EMAIL, getString(R.string.supp_message_address))
                this.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.supp_message_theme))
                this.putExtra(Intent.EXTRA_TEXT, getString(R.string.supp_message_content))
            })
        }
    }

    private fun configureShareButton() {
        binding.share.setOnClickListener {
            startActivity(Intent(Intent.ACTION_SEND).apply {
                this.type = "text/plain"
                this.putExtra(Intent.EXTRA_TEXT, getString(R.string.android_developer))
            })
        }
    }

    private fun configureUserAgreementButton() {
        binding.userAgreement.setOnClickListener {
            val url = Uri.parse(getString(R.string.practicum_offer))
            startActivity(Intent(Intent.ACTION_VIEW, url))
        }
    }

    private fun configureThemeSwitcher() {
        val app = application as App
        with(binding.darkTheme) {
            isChecked = app.themeCode == 2
            setOnCheckedChangeListener { _, checked ->
                app.switchTheme(checked)
            }
        }
    }
}