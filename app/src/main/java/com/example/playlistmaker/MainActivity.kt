package com.example.playlistmaker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.View.OnClickListener
import android.widget.Button

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val settingsButton = findViewById<Button>(R.id.settings)
        val mediaButton = findViewById<Button>(R.id.media)
        val searchButton = findViewById<Button>(R.id.search)

        searchButton.setOnClickListener {
            startActivity(Intent(this, SearchActivity::class.java))
        }

        val buttonOnClickListener = object : OnClickListener {
            override fun onClick(v: View?) {
                startActivity(Intent(this@MainActivity, MediaActivity::class.java))
            }
        }
        mediaButton.setOnClickListener(buttonOnClickListener)

        settingsButton.setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
        }

    }
}
