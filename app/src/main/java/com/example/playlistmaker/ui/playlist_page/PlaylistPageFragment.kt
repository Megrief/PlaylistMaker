package com.example.playlistmaker.ui.playlist_page

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
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
import com.example.playlistmaker.ui.audioplayer.adapter.PlaylistLineViewHolder
import com.example.playlistmaker.ui.main.RootActivity
import com.example.playlistmaker.ui.playlist_page.screen_state.PlaylistPageScreenState
import com.example.playlistmaker.ui.playlist_page.view_model.PlaylistPageViewModel
import com.example.playlistmaker.ui.search.adapter.TrackAdapter
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetBehavior.BottomSheetCallback
import com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_COLLAPSED
import com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_HIDDEN
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistPageFragment : Fragment() {

    private val viewModel: PlaylistPageViewModel by viewModel()

    private var _binding: FragmentPlaylistPageBinding? = null
    private val binding: FragmentPlaylistPageBinding
        get() = _binding!!
    private var onTrackListContent: ((Boolean) -> Unit)? = null
    private var onChangeMenuState: (() -> Unit)? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
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
        configureTrackListBottomSheetView()
        configureMenuBottomSheetView()
        configureShareButton()
        configureMenu()
    }

    override fun onResume() {
        super.onResume()
        viewModel.checkState()
        with((requireActivity() as RootActivity).binding.bottomGroup) {
            if (isVisible) visibility = GONE
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun setObserver() {
        viewModel.screenState.observe(viewLifecycleOwner) { screenState ->
            when (screenState) {
                is PlaylistPageScreenState.Loading -> { }
                is PlaylistPageScreenState.Error -> { }
                is PlaylistPageScreenState.Content -> {
                    if (screenState.playlist != null) {
                        setPhoto(screenState.playlist.photoId)
                        setTrackListContent(screenState.trackList)
                        setTextInfo(screenState.playlist, screenState.totalLength, screenState.totalTracks)
                        setPlaylistLineInMenu(screenState.playlist)
                    } else requireActivity().onBackPressedDispatcher.onBackPressed()
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
        onTrackListContent?.invoke(trackList.isEmpty())
        (binding.tracksList.adapter as? TrackAdapter)?.setTrackList(trackList)
    }

    private fun configureMenu() {
        binding.menu.setOnClickListener {
            onChangeMenuState?.invoke()
        }
        with(binding.menuContent) {
            sharePlaylist.setOnClickListener {
                onShare()
                onChangeMenuState?.invoke()
            }
            editPlaylist.setOnClickListener {
                onChangeMenuState?.invoke()
                viewModel.storePlaylistId()
                findNavController().navigate(R.id.action_playlistPageFragment_to_playlistEditFragment)
            }
            deletePlaylist.setOnClickListener {
                viewModel.showOnDeletePlaylistDialog(requireContext())
                onChangeMenuState?.invoke()
            }
        }
    }

    private fun setPlaylistLineInMenu(playlist: Playlist) {
        with(binding.menuContent.playlistLine) {
            val lineVH = PlaylistLineViewHolder(this)
            lineVH.bind(playlist)
        }
    }

    private fun configureShareButton() {
        binding.share.setOnClickListener {
            onShare()
        }
    }

    private fun onShare() {
        if (viewModel.sharePlaylist())
            Toast.makeText(requireContext(), getString(R.string.no_content_to_share), Toast.LENGTH_LONG).show()
    }

    private fun configureTrackList() {
        val onTrackClicked: (Track) -> Unit = {
            viewModel.storeTrack(it)
            findNavController().navigate(R.id.action_playlistPageFragment_to_audioplayerFragment)
        }
        val onTrackLongClicked: (Track) -> Boolean = {
            viewModel.showOnDeleteTrackDialog(it, requireContext())
            true
        }
        binding.tracksList.adapter = TrackAdapter(
            onTrackClicked = onTrackClicked,
            onTrackLongClicked = onTrackLongClicked
        )
    }

    private fun configureTrackListBottomSheetView() {
        val bottomSheetCallback = object : BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                binding.shadow.isGone = newState == STATE_COLLAPSED || newState == STATE_HIDDEN
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) { }
        }
        val bottomSheetBehavior = BottomSheetBehavior.from(binding.bottomSheet)
        bottomSheetBehavior.state = STATE_COLLAPSED
        val peekHeight = resources.getDimensionPixelSize(R.dimen.track_list_peek_height)
        bottomSheetBehavior.peekHeight = peekHeight
        bottomSheetBehavior.addBottomSheetCallback(bottomSheetCallback)
        onTrackListContent = { isEmpty ->
            bottomSheetBehavior.isHideable = isEmpty
            if (isEmpty) bottomSheetBehavior.state = STATE_HIDDEN
        }
    }

    private fun configureMenuBottomSheetView() {
        val bottomSheetCallback = object : BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                binding.shadow.isGone = newState == STATE_HIDDEN
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) { }
        }

        val bottomSheetBehavior = BottomSheetBehavior.from(binding.bottomSheetMenu)
        bottomSheetBehavior.state = STATE_HIDDEN
        val peekHeight = resources.getDimensionPixelSize(R.dimen.menu_peek_height)
        bottomSheetBehavior.peekHeight = peekHeight
        bottomSheetBehavior.addBottomSheetCallback(bottomSheetCallback)
        onChangeMenuState = {
            val currentState = bottomSheetBehavior.state
            bottomSheetBehavior.state = if (currentState == STATE_HIDDEN) STATE_COLLAPSED else STATE_HIDDEN
        }
    }

}