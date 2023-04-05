package com.example.playlistmaker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.View.OnClickListener
import android.widget.Button
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val settings = findViewById<Button>(R.id.settings)
        val media = findViewById<Button>(R.id.media)
        val search = findViewById<Button>(R.id.search)

        search.setOnClickListener {
            Toast.makeText(this, "Looking for something...", Toast.LENGTH_LONG).show()
        }

        val buttonOnClickListener = object : OnClickListener {
            override fun onClick(v: View?) {
                Toast.makeText(this@MainActivity, "Open media", Toast.LENGTH_SHORT).show()
            }
        }
        media.setOnClickListener(buttonOnClickListener)

        settings.setOnClickListener {
            Toast.makeText(this, "Open settings", Toast.LENGTH_SHORT).show()
        }

    }
}
