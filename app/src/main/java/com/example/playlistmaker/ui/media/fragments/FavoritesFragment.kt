package com.example.playlistmaker.ui.media.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentFavoritesBinding
import com.example.playlistmaker.domain.entities.Track
import com.example.playlistmaker.ui.main.RootActivity
import com.example.playlistmaker.ui.media.fragments.screen_states.MediaScreenState
import com.example.playlistmaker.ui.media.fragments.view_models.FavoritesViewModel
import com.example.playlistmaker.ui.search.SearchFragment
import com.example.playlistmaker.ui.search.adapter.TrackAdapter
import com.example.playlistmaker.utils.debounce
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavoritesFragment : Fragment() {

    private val viewModel: FavoritesViewModel by viewModel()

    private var _binding: FragmentFavoritesBinding? = null
    private val binding: FragmentFavoritesBinding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setAdapter()
        viewModel.screenState.observe(viewLifecycleOwner) { screenState ->
            hide()
            when (screenState) {
                is MediaScreenState.Loading -> with(binding) {
                    loading.visibility = VISIBLE
                }
                is MediaScreenState.Empty -> {
                    binding.noContent.visibility = VISIBLE
                }
                is MediaScreenState.Content -> with(binding) {
                    val trackList = screenState.content.mapNotNull {
                        try { it as Track } catch (_: ClassCastException) { null }
                    }
                    (favoritesListRv.adapter as TrackAdapter).setTrackList(trackList)
                    favoritesListRv.visibility = VISIBLE
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.checkContent((binding.favoritesListRv.adapter as TrackAdapter).trackList)
        with((requireActivity() as RootActivity).binding.bottomNav) {
            if (isGone) visibility = VISIBLE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.favoritesListRv.adapter = null
        _binding = null

    }

    private fun hide() {
        with(binding) {
            noContent.visibility = GONE
            favoritesListRv.visibility = GONE
            loading.visibility = GONE
        }
    }

    private fun setAdapter() {
        val onTrackClicked = debounce<Track>(
            SearchFragment.CLICK_DEBOUNCE_DELAY,
            lifecycleScope,
            false
        ) { track ->
            viewModel.onTrackClick(track)
            (requireActivity() as RootActivity).binding.bottomNav.visibility = GONE
            findNavController().navigate(R.id.action_mediaFragment_to_audioplayerActivity)
        }
        binding.favoritesListRv.adapter = TrackAdapter(onTrackClicked)
    }
}