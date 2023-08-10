package com.example.playlistmaker.ui.audioplayer

import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.core.view.isGone
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivityAudioplayerBinding
import com.example.playlistmaker.domain.entities.Track
import com.example.playlistmaker.ui.audioplayer.view_model.AudioplayerScreenState
import com.example.playlistmaker.ui.audioplayer.view_model.AudiolayerViewModel
import com.example.playlistmaker.ui.audioplayer.view_model.player.PlayerStatus
import java.util.Locale

class AudioplayerActivity : ComponentActivity() {
    private val binding by lazy { ActivityAudioplayerBinding.inflate(LayoutInflater.from(this)) }
    private lateinit var viewModel: AudiolayerViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        // при смене темы на устройстве пересоздается активити,
        // ставится на паузу воспроизведение и сбрасывается время воспроизведения
        viewModel = ViewModelProvider(this, AudiolayerViewModel.getViewModelFactory())[AudiolayerViewModel::class.java]

        viewModel.getScreenStateLiveData().observe(this) {
            when (it) {
                is AudioplayerScreenState.Error -> onBackPressedDispatcher
                is AudioplayerScreenState.Loading -> binding.playButton.isEnabled = false
                is AudioplayerScreenState.Content -> {
                    onSuccess(it.track)
                }

            }
        }

        viewModel.getPlayerStatusLiveData().observe(this) {
            if (it is PlayerStatus.Playing) {
                binding.playingTime.text = it.currentPosition
                changeButtonAppearance(true)
            } else changeButtonAppearance(false)
        }

        binding.toolbar.setNavigationOnClickListener { onBackPressedDispatcher.onBackPressed() }

        binding.playButton.setOnClickListener { viewModel.playback() }
    }

    override fun onPause() {
        super.onPause()
        viewModel.pause()
    }

    private fun changeButtonAppearance(isPlaying: Boolean) {
        binding.playButton.setImageResource(if (isPlaying) R.drawable.pause_icon else R.drawable.play_icon)
    }
    private fun onSuccess(track: Track) {
        val mainHandler = Handler(Looper.getMainLooper())
        mainHandler.post {
            binding.playButton.isEnabled = true
            bind(track)
            if (track.previewUrl.isEmpty()) {
                Toast.makeText(this, getString(R.string.no_preview), Toast.LENGTH_LONG).show()
                binding.playButton.isEnabled = false
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