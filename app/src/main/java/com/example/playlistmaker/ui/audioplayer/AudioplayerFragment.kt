package com.example.playlistmaker.ui.audioplayer

import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedDispatcher
import androidx.core.view.isGone
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentAudioplayerBinding
import com.example.playlistmaker.domain.entities.Track
import com.example.playlistmaker.ui.audioplayer.view_model.AudioplayerScreenState
import com.example.playlistmaker.ui.audioplayer.view_model.AudioplayerViewModel
import com.example.playlistmaker.ui.audioplayer.view_model.player.PlayerStatus
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.Locale

class AudioplayerFragment : Fragment() {

    private var _binding: FragmentAudioplayerBinding? = null
    private val binding: FragmentAudioplayerBinding
        get() = _binding!!

    private var onBackPressedDispatcher: OnBackPressedDispatcher? = null
    private val viewModel: AudioplayerViewModel by viewModel()

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

        binding.toolbar.setNavigationOnClickListener { onBackPressedDispatcher?.onBackPressed() }

        binding.playButton.setOnClickListener { viewModel.playback() }

        binding.likeButton.setOnClickListener { viewModel.likeback() }

        binding.addToPlaylist.setOnClickListener {
            //TODO("open bottom sheet with playlists")
        }
        // TODO("add bottom sheet with playlists and <create playlist> button")
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

    private fun setScreenStateObserver() {
        viewModel.screenState.observe(viewLifecycleOwner) { screenState ->
            when (screenState) {
                is AudioplayerScreenState.Error -> onBackPressedDispatcher
                is AudioplayerScreenState.Loading -> {
                    binding.likeButton.isEnabled = false
                    binding.playButton.isEnabled = false
                }
                is AudioplayerScreenState.Content -> {
                    onSuccess(screenState.track, screenState.inFavourite)
                }
            }
        }
    }

    private fun setPlayerStatusObserver() {
        viewModel.playerStatus.observe(viewLifecycleOwner) { playerStatus ->
            when (playerStatus) {
                is PlayerStatus.Prepared, is PlayerStatus.Paused -> {
                    if (playerStatus is PlayerStatus.Prepared)
                        binding.playingTime.text = playerStatus.currentPosition ?: getString(R.string.half_minute)
                    changeButtonAppearance(false)
                }
                is PlayerStatus.Playing -> {
                    binding.playingTime.text = playerStatus.currentPosition
                    changeButtonAppearance(true)
                }
                is PlayerStatus.Default -> { }
            }
        }
    }

    private fun changeButtonAppearance(isPlaying: Boolean) {
        binding.playButton.setImageResource(if (isPlaying) R.drawable.pause_icon else R.drawable.play_icon)
    }

    private fun onSuccess(track: Track, inFavourite: Boolean) {
        binding.playButton.isEnabled = true
        binding.likeButton.isEnabled = true
        Glide.with(this)
            .load(if (inFavourite) R.drawable.favorite else R.drawable.not_favourite)
            .into(binding.likeButton)
        bind(track)
        if (track.previewUrl.isEmpty()) {
            Toast.makeText(requireContext(), getString(R.string.no_preview), Toast.LENGTH_LONG).show()
            binding.playButton.isEnabled = false
        }
    }

    private fun bind(track: Track) {
        val cornerSizeInPx = resources.getDimensionPixelSize(R.dimen.small)
        if (track.artworkUrl100.isNotEmpty()) {
            Glide.with(binding.root)
                .load(getPoster512(track.artworkUrl100))
                .placeholder(R.drawable.placeholder)
                .transform(CenterCrop(), RoundedCorners(cornerSizeInPx))
                .into(binding.poster)
        }
        with(binding) {
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