package com.example.playlistmaker.ui.media.vp_adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.playlistmaker.ui.media.fragments.PlaylistsFragment
import com.example.playlistmaker.ui.media.fragments.FavoritesFragment

class AdapterVP(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle
) : FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun getItemCount() = 2

    override fun createFragment(position: Int): Fragment {
       return when (position) {
           0 -> FavoritesFragment()
           else -> PlaylistsFragment.newInstance()
       }
    }

}