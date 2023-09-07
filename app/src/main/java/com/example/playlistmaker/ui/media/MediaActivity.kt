package com.example.playlistmaker.ui.media

import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivityMediaBinding
import com.example.playlistmaker.ui.media.vp_adapter.AdapterVP
import com.google.android.material.tabs.TabLayoutMediator

class MediaActivity : AppCompatActivity() {
    private val binding by lazy { ActivityMediaBinding.inflate(LayoutInflater.from(this)) }
    private val tabMediator by lazy {
        TabLayoutMediator(binding.mediaTl, binding.mediaVp) { tab,position ->
            tab.text =
                if (position == 0) getString(R.string.favourite_tracks)
                else getString(R.string.playlists)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
        binding.mediaVp.adapter = AdapterVP(supportFragmentManager, lifecycle)
        tabMediator.attach()

    }

    override fun onDestroy() {
        super.onDestroy()
        tabMediator.detach()
    }

}