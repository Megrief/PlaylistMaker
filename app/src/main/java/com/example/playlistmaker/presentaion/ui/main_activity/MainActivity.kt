package com.example.playlistmaker.presentaion.ui.main_activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.playlistmaker.R
import com.example.playlistmaker.presentaion.ui.media.MediaActivity
import com.example.playlistmaker.presentaion.ui.settings.SettingsActivity
import com.example.playlistmaker.presentaion.ui.search.SearchActivity

class MainActivity : AppCompatActivity() {
    private val settingsButton by lazy { findViewById<Button>(R.id.settings) }
    private val mediaButton by lazy { findViewById<Button>(R.id.media) }
    private val searchButton by lazy { findViewById<Button>(R.id.search) }

    override fun onCreate(savedInstanceState: Bundle?) {
        this.theme.changingConfigurations
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        searchButton.setOnClickListener {
            startActivity(Intent(this, SearchActivity::class.java))
        }
        mediaButton.setOnClickListener {
            startActivity(Intent(this, MediaActivity::class.java))
        }
        settingsButton.setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
        }
    }
}
