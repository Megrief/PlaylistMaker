package com.example.playlistmaker.ui.audioplayer

import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isGone
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivityAudioplayerBinding
import com.example.playlistmaker.domain.entity.Track
import com.example.playlistmaker.ui.audioplayer.view_model.AudioplayerScreenState
import com.example.playlistmaker.ui.audioplayer.view_model.AudioplayerViewModel
import com.example.playlistmaker.ui.audioplayer.view_model.player.PlayerStatus
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.Locale

class AudioplayerActivity : AppCompatActivity() {
    private val binding by lazy { ActivityAudioplayerBinding.inflate(LayoutInflater.from(this)) }

    private val viewModel: AudioplayerViewModel by viewModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setScreenStateObserver()

        setPlayerStatusObserver()

        binding.toolbar.setNavigationOnClickListener { onBackPressedDispatcher.onBackPressed() }

        binding.playButton.setOnClickListener { viewModel.playback() }
    }

    override fun onPause() {
        super.onPause()
        viewModel.pause()
    }

    private fun setScreenStateObserver() {
        viewModel.screenState.observe(this) { screenState ->
            lifecycleScope.launch(Dispatchers.Main) {
                when (screenState) {
                    is AudioplayerScreenState.Error -> onBackPressedDispatcher
                    is AudioplayerScreenState.Loading -> binding.playButton.isEnabled = false
                    is AudioplayerScreenState.Content -> {
                        onSuccess(screenState.track)
                    }
                }
            }
        }
    }

    private fun setPlayerStatusObserver() {
        viewModel.playerStatus.observe(this) { playerStatus ->
            when (playerStatus) {
                is PlayerStatus.Prepared, is PlayerStatus.Paused -> {
                    if (playerStatus is PlayerStatus.Prepared) {
                        binding.playingTime.text = playerStatus.currentPosition ?: getString(R.string.half_minute)
                    }
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

    private fun onSuccess(track: Track) {
        binding.playButton.isEnabled = true
        bind(track)
        if (track.previewUrl.isEmpty()) {
            Toast.makeText(this, getString(R.string.no_preview), Toast.LENGTH_LONG).show()
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