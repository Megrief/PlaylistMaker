package com.example.playlistmaker.ui.media

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentMediaBinding
import com.example.playlistmaker.ui.media.vp_adapter.AdapterVP
import com.google.android.material.tabs.TabLayoutMediator

class MediaFragment : Fragment() {
    private var _binding: FragmentMediaBinding? = null
    private val binding: FragmentMediaBinding
        get() = _binding!!


    private var _tabMediator: TabLayoutMediator? = null
    private val tabMediator: TabLayoutMediator
        get() = _tabMediator!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMediaBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _tabMediator = TabLayoutMediator(binding.mediaTl, binding.mediaVp) { tab,position ->
            tab.text =
                if (position == 0) getString(R.string.favourite_tracks)
                else getString(R.string.playlists)
        }
        binding.mediaVp.adapter = AdapterVP(childFragmentManager, lifecycle)
        tabMediator.attach()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        tabMediator.detach()
        _binding = null
    }

}