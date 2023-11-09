package com.example.playlistmaker.ui.media.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isGone
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentPlaylistsBinding
import com.example.playlistmaker.domain.entities.Playlist
import com.example.playlistmaker.ui.main.RootActivity
import com.example.playlistmaker.ui.media.adapters.PlaylistGridAdapter
import com.example.playlistmaker.ui.media.fragments.screen_states.MediaScreenState
import com.example.playlistmaker.ui.media.fragments.view_models.PlaylistsViewModel
import com.example.playlistmaker.ui.search.SearchFragment
import com.example.playlistmaker.utils.debounce
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistsFragment : Fragment() {

    private val viewModel: PlaylistsViewModel by viewModel()
    private var _binding: FragmentPlaylistsBinding? = null
    private val binding: FragmentPlaylistsBinding
        get() = _binding!!

    private val onPlaylistClick = debounce<Playlist>(
        SearchFragment.CLICK_DEBOUNCE_DELAY,
        lifecycleScope,
        false
    ) { playlist ->
        Toast.makeText(requireContext(), "Clicked!", Toast.LENGTH_SHORT).show()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlaylistsBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        configureCreateButton()
        configurePlaylistsRv()
        setScreenStateObserver()
    }

    override fun onResume() {
        super.onResume()
        viewModel.checkContent((binding.playlistsRv.adapter as PlaylistGridAdapter).contentList)
        with((requireActivity() as RootActivity).binding.bottomNav) {
            if (isGone) visibility = VISIBLE
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun configurePlaylistsRv() {
        with(binding.playlistsRv) {
            layoutManager = GridLayoutManager(requireContext(), 2)
            adapter = PlaylistGridAdapter(onPlaylistClick)
        }
    }

    private fun configureCreateButton() {
        binding.createPlaylist.setOnClickListener {
            findNavController().navigate(R.id.action_mediaFragment_to_playlistCreationFragment)
        }
    }

    private fun setScreenStateObserver() {
        viewModel.screenState.observe(viewLifecycleOwner) { screenState ->
            hideAll()
            when (screenState) {
                is MediaScreenState.Loading -> with(binding) {
                    progressBar.visibility = VISIBLE
                }
                is MediaScreenState.Empty -> with(binding) {
                    nothingFoundIv.visibility = VISIBLE
                    messageTv.visibility = VISIBLE
                    createPlaylist.visibility = VISIBLE
                }
                is MediaScreenState.Content -> with(binding) {
                    val playlistsList = screenState.content.mapNotNull { it as? Playlist }
                    (playlistsRv.adapter as PlaylistGridAdapter).setContent(playlistsList)
                    playlistsRv.visibility = VISIBLE
                    createPlaylist.visibility = VISIBLE
                }
            }
        }
    }

    private fun hideAll() {
        with(binding) {
            playlistsRv.visibility = GONE
            progressBar.visibility = GONE
            messageTv.visibility = GONE
            nothingFoundIv.visibility = GONE
            createPlaylist.visibility = GONE
        }
    }
}