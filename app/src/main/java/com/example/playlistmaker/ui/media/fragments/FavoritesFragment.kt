package com.example.playlistmaker.ui.media.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentFavoritesBinding
import com.example.playlistmaker.domain.entity.Track
import com.example.playlistmaker.ui.media.fragments.screen_states.FavoritesScreenState
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
            when (screenState) {
                is FavoritesScreenState.Loading -> with(binding) {
                    hide()
                    loading.visibility = VISIBLE
                }
                is FavoritesScreenState.Content -> with(binding) {
                    hide()
                    (favouritesListRv.adapter as TrackAdapter).setTrackList(screenState.content)
                    if (screenState.content.isEmpty()) noContent.visibility = VISIBLE
                    else favouritesListRv.visibility = VISIBLE
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.checkContent((binding.favouritesListRv.adapter as TrackAdapter).trackList)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.favouritesListRv.adapter = null
        _binding = null

    }

    private fun hide() {
        with(binding) {
            noContent.visibility = GONE
            favouritesListRv.visibility = GONE
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
            findNavController().navigate(R.id.action_mediaFragment_to_audioplayerActivity)
        }
        binding.favouritesListRv.adapter = TrackAdapter(onTrackClicked)
    }
}