package com.example.playlistmaker.ui.media.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentPlaylistsBinding
import com.example.playlistmaker.ui.main.RootActivity
import com.example.playlistmaker.ui.media.fragments.view_models.PlaylistsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistsFragment : Fragment() {

    private val viewModel: PlaylistsViewModel by viewModel()
    private var _binding: FragmentPlaylistsBinding? = null
    private val binding: FragmentPlaylistsBinding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlaylistsBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.createPlaylist.setOnClickListener {
            (requireActivity() as RootActivity).binding.bottomNav.visibility = GONE
            findNavController().navigate(R.id.action_mediaFragment_to_playlistCreationFragment)
        }
    }

    override fun onResume() {
        super.onResume()
        with((requireActivity() as RootActivity).binding.bottomNav) {
            if (isGone) visibility = VISIBLE
        }
    }


    companion object {
        fun newInstance(): PlaylistsFragment {
            return PlaylistsFragment()
        }

    }
}