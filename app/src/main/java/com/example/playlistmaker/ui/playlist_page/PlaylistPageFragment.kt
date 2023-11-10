package com.example.playlistmaker.ui.playlist_page

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentPlaylistPageBinding
import com.example.playlistmaker.domain.entities.Playlist
import com.example.playlistmaker.domain.entities.Track
import com.example.playlistmaker.ui.main.RootActivity
import com.example.playlistmaker.ui.playlist_page.screen_state.PlaylistPageScreenState
import com.example.playlistmaker.ui.playlist_page.view_model.PlaylistPageViewModel
import com.example.playlistmaker.ui.search.adapter.TrackAdapter
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetBehavior.BottomSheetCallback
import com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_COLLAPSED
import com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_HIDDEN
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistPageFragment : Fragment() {

    private val viewModel: PlaylistPageViewModel by viewModel()

    private var _binding: FragmentPlaylistPageBinding? = null
    private val binding: FragmentPlaylistPageBinding
        get() = _binding!!
    private var bottomSheetBehavior: BottomSheetBehavior<LinearLayout>? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlaylistPageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.toolbar.setNavigationOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
        configureTrackList()
        setObserver()
        configureBottomSheetView()
        configureShareButton()
    }

    override fun onResume() {
        super.onResume()
        with((requireActivity() as RootActivity).binding.bottomNav) {
            if (isVisible) visibility = GONE
        }
    }

    private fun setObserver() {
        viewModel.screenState.observe(viewLifecycleOwner) { screenState ->
            when (screenState) {
                is PlaylistPageScreenState.Loading -> { }
                is PlaylistPageScreenState.Error -> { }
                is PlaylistPageScreenState.Content -> {
                    setPhoto(screenState.playlist.photoId)
                    setTrackListContent(screenState.trackList)
                    setTextInfo(screenState.playlist, screenState.totalLength, screenState.totalTracks)
                }
            }
        }
    }

    private fun setTextInfo(playlist: Playlist, totalLength: String, totalTracks: String) {
        with(binding) {
            playlistsName.text = playlist.name
            playlistsDescription.text = playlist.description
            playlistsTotalLength.text = totalLength
            playlistsNumOfTracks.text = totalTracks
        }
    }

    private fun setPhoto(id: Long) {
        lifecycleScope.launch(Dispatchers.IO) {
            val uri = viewModel.getPhoto(id)
            withContext(Dispatchers.Main) {
                Glide.with(binding.root)
                    .load(uri)
                    .centerCrop()
                    .placeholder(R.drawable.placeholder)
                    .into(binding.playlistsPhoto)
            }
        }
    }

    private fun setTrackListContent(trackList: List<Track>) {
        bottomSheetBehavior?.run {
            isHideable = trackList.isEmpty()
            state = if (trackList.isEmpty()) STATE_HIDDEN else STATE_COLLAPSED
        }
        (binding.tracksList.adapter as? TrackAdapter)?.setTrackList(trackList)
    }

    private fun configureShareButton() {
        binding.share.setOnClickListener {
            if (bottomSheetBehavior?.state == STATE_HIDDEN) {
                Toast.makeText(requireContext(),
                    getString(R.string.no_content_to_share), Toast.LENGTH_LONG).show()
            } else {
                viewModel.sharePlaylist()
            }
        }
    }

    private fun configureTrackList() {
        val onTrackClicked: (Track) -> Unit = {
            viewModel.storeTrack(it)
            findNavController().navigate(R.id.action_playlistPageFragment_to_audioplayerFragment)
        }
        val onTrackLongClicked: (Track) -> Boolean = {
            showOnDeleteTrackDialog(it)
            true
        }
        binding.tracksList.adapter = TrackAdapter(
            onTrackClicked = onTrackClicked,
            onTrackLongClicked = onTrackLongClicked
        )
    }

    private fun showOnDeleteTrackDialog(track: Track) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Удалить трек")
            .setMessage("Хотите удалить трек?")
            .setNegativeButton("Нет") { _, _ -> }
            .setPositiveButton("Да") { _, _ ->
                viewModel.deleteTrack(track)
            }.show()
    }
    private fun configureBottomSheetView() {
        val bottomSheetCallback = object : BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                binding.shadow.isGone = newState == STATE_COLLAPSED || newState == STATE_HIDDEN
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {

            }
        }
        bottomSheetBehavior = BottomSheetBehavior.from(binding.bottomSheet)
        bottomSheetBehavior?.state = STATE_COLLAPSED
        bottomSheetBehavior?.addBottomSheetCallback(bottomSheetCallback)
    }

// TODO("
//  1. Add BottomSheet for menu
//  2. Add new fragment for editing playlists info (use PlaylistCreation layout)
//  ")
}