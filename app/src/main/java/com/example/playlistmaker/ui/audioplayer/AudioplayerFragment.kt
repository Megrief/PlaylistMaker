package com.example.playlistmaker.ui.audioplayer

import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedDispatcher
import androidx.core.view.isGone
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentAudioplayerBinding
import com.example.playlistmaker.domain.entities.Playlist
import com.example.playlistmaker.domain.entities.Track
import com.example.playlistmaker.ui.audioplayer.adapter.PlaylistLineAdapter
import com.example.playlistmaker.ui.audioplayer.view_model.AudioplayerScreenState
import com.example.playlistmaker.ui.audioplayer.view_model.AudioplayerViewModel
import com.example.playlistmaker.ui.audioplayer.view_model.player.PlayerStatus
import com.example.playlistmaker.utils.isEquals
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetBehavior.BottomSheetCallback
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.Locale

class AudioplayerFragment : Fragment() {

    private var _binding: FragmentAudioplayerBinding? = null
    private val binding: FragmentAudioplayerBinding
        get() = _binding!!

    private var onBackPressedDispatcher: OnBackPressedDispatcher? = null
    private val viewModel: AudioplayerViewModel by viewModel()
    private var bottomSheetBehavior: BottomSheetBehavior<LinearLayout>? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAudioplayerBinding.inflate(inflater, container, false)
        onBackPressedDispatcher = requireActivity().onBackPressedDispatcher
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setScreenStateObserver()
        setPlayerStatusObserver()
        setOnTrackInPlaylistObserver()
        configureBottomSheet()
        configureBottomSheetContent()

        binding.toolbar.setNavigationOnClickListener { onBackPressedDispatcher?.onBackPressed() }

        with(binding.content) {
            playButton.setOnClickListener { viewModel.playback() }
            likeButton.setOnClickListener { viewModel.likeback() }
            addToPlaylist.setOnClickListener {
                bottomSheetBehavior?.state = BottomSheetBehavior.STATE_COLLAPSED
            }
        }
    }

    override fun onPause() {
        super.onPause()
        viewModel.stop()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        onBackPressedDispatcher = null
        _binding = null
    }

    private fun setOnTrackInPlaylistObserver() {
        viewModel.trackInPlaylist.observe(viewLifecycleOwner) { status ->

            when (status?.second) {
                true -> {
                    bottomSheetBehavior?.state = BottomSheetBehavior.STATE_HIDDEN
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.added_to_playlist) + " " + status.first,
                        Toast.LENGTH_SHORT
                    ).show()
                }
                false -> {
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.already_added) + " " + status.first,
                        Toast.LENGTH_SHORT
                    ).show()
                }
                else -> { }
            }

        }
    }

    private fun configureBottomSheet() {
        val bottomSheetCallback = object : BottomSheetCallback() {

            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_HIDDEN -> {
                        binding.shadow.visibility = GONE
                        binding.root.isEnabled = true
                    }
                    BottomSheetBehavior.STATE_COLLAPSED -> {
                        binding.shadow.visibility = VISIBLE
                        binding.root.isEnabled = false
                        lifecycleScope.launch(Dispatchers.IO) {
                            val list = viewModel.getPlaylists().single()
                            withContext(Dispatchers.Main) {
                                (binding.playlistsList.adapter as? PlaylistLineAdapter)?.run {
                                    if(!isEquals(contentList, list)) {
                                        setContent(list)
                                    }
                                }
                            }
                        }
                    }
                    else -> { }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) { }
        }
        bottomSheetBehavior = BottomSheetBehavior.from(binding.bottomSheet)
        bottomSheetBehavior?.state = BottomSheetBehavior.STATE_HIDDEN
        bottomSheetBehavior?.addBottomSheetCallback(bottomSheetCallback)
    }

    private fun configureBottomSheetContent() {
        binding.createPlaylist.setOnClickListener {
            lifecycleScope.launch {
                bottomSheetBehavior?.state = BottomSheetBehavior.STATE_HIDDEN
                delay(150)
                findNavController().navigate(R.id.action_audioplayerFragment_to_playlistCreationFragment)
            }
        }
        binding.playlistsList.adapter = PlaylistLineAdapter { playlist ->
            viewModel.addToPlaylist(playlist)
        }
    }

    private fun setScreenStateObserver() {
        viewModel.screenState.observe(viewLifecycleOwner) { screenState ->
            when (screenState) {
                is AudioplayerScreenState.Error -> onBackPressedDispatcher
                is AudioplayerScreenState.Loading -> {
                    binding.content.likeButton.isEnabled = false
                    binding.content.playButton.isEnabled = false
                }
                is AudioplayerScreenState.Content ->
                    onSuccess(screenState.track, screenState.inFavourite, screenState.playlists)
            }
        }
    }

    private fun setPlayerStatusObserver() {
        viewModel.playerStatus.observe(viewLifecycleOwner) { playerStatus ->
            when (playerStatus) {
                is PlayerStatus.Prepared, is PlayerStatus.Paused -> {
                    if (playerStatus is PlayerStatus.Prepared)
                        binding.content.playingTime.text = playerStatus.currentPosition ?: getString(
                            R.string.half_minute)
                    changeButtonAppearance(false)
                }
                is PlayerStatus.Playing -> {
                    binding.content.playingTime.text = playerStatus.currentPosition
                    changeButtonAppearance(true)
                }
                is PlayerStatus.Default -> { }
            }
        }
    }

    private fun changeButtonAppearance(isPlaying: Boolean) {
        binding.content.playButton.setImageResource(if (isPlaying) R.drawable.pause_icon else R.drawable.play_icon)
    }

    private fun onSuccess(track: Track, inFavourite: Boolean, playlists: List<Playlist>) {
        (binding.playlistsList.adapter as? PlaylistLineAdapter)?.setContent(playlists)
        with(binding.content) {
            playButton.isEnabled = true
            likeButton.isEnabled = true
            Glide.with(binding.root)
                .load(if (inFavourite) R.drawable.favorite else R.drawable.not_favourite)
                .into(likeButton)
            bind(track)
            if (track.previewUrl.isEmpty()) {
                Toast.makeText(requireContext(), getString(R.string.no_preview), Toast.LENGTH_LONG).show()
                playButton.isEnabled = false
            }
        }

    }

    private fun bind(track: Track) {
        val cornerSizeInPx = resources.getDimensionPixelSize(R.dimen.small)
        if (track.artworkUrl100.isNotEmpty()) {
            Glide.with(binding.root)
                .load(getPoster512(track.artworkUrl100))
                .placeholder(R.drawable.placeholder)
                .transform(CenterCrop(), RoundedCorners(cornerSizeInPx))
                .into(binding.content.poster)
        }
        with(binding.content) {
            bindItemText(trackName, track.trackName, trackName)
            bindItemText(artistName, track.artistName, artistName)
            bindItemText(trackLengthValue, getLength(track.trackTime), trackLengthGroup)
            bindItemText(collectionValue, track.collectionName, collectionGroup)
            bindItemText(releaseYearValue, track.releaseDate, releaseYearGroup)
            bindItemText(genreValue, track.primaryGenreName, genreGroup)
            bindItemText(countryValue, track.country, countryGroup)
        }

    }

    private fun bindItemText(view: TextView, text: String, group: View) {
        if (isNotEmpty(text, group)) view.text = text
    }

    private fun isNotEmpty(str: String, group: View): Boolean {
        return if (str.isBlank()) {
            group.isGone = true
            false
        } else true
    }

    private fun getPoster512(url: String): String = url.replaceAfterLast('/', "512x512bb.jpg")

    private fun getLength(time: Long): String = SimpleDateFormat("mm:ss", Locale.getDefault()).format(time)
}