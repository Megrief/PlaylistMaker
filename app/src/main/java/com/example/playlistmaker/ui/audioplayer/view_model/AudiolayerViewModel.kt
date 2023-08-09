package com.example.playlistmaker.ui.audioplayer.view_model

import android.icu.text.SimpleDateFormat
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.playlistmaker.app.App
import com.example.playlistmaker.domain.entities.Track
import com.example.playlistmaker.utils.creator.Creator
import com.example.playlistmaker.domain.use_cases.GetDataUseCase
import com.example.playlistmaker.ui.audioplayer.view_model.player.Player
import com.example.playlistmaker.ui.audioplayer.view_model.player.PlayerStatus
import com.example.playlistmaker.ui.search.view_model.SearchViewModel
import java.util.Locale

class AudiolayerViewModel(
    getDataUseCase: GetDataUseCase<Track?>,
    application: App
) : AndroidViewModel(application) {

    private val screenStateLiveData = MutableLiveData<AudioplayerScreenState>(AudioplayerScreenState.Loading)
    private val playerStatusLiveData = MutableLiveData<PlayerStatus>(PlayerStatus.Default)
    private val mainHandler = Handler(Looper.getMainLooper())
    private lateinit var player: Player

    private val refreshPosition = object : Runnable {
        override fun run() {
            val value = getPlayerStatusLiveData().value
            if (value is PlayerStatus.Playing) {
                playerStatusLiveData.value = value.copy(
                    currentPosition = getLength(player.getCurrentPosition())
                )
            } else mainHandler.removeCallbacks(this)

            mainHandler.postDelayed(this, DELAY_MILLIS)
        }
    }

    init {
        getDataUseCase.get(SearchViewModel.TRACK) {
            screenStateLiveData.postValue(
                if (it != null) {
                    player = Player(
                        it.previewUrl,
                        { playerStatusLiveData.value = PlayerStatus.Prepared },
                        { playerStatusLiveData.value = PlayerStatus.Prepared }
                    )
                    AudioplayerScreenState.Content(it)
                } else AudioplayerScreenState.Error
            )
        }
    }

    fun getScreenStateLiveData(): LiveData<AudioplayerScreenState> = screenStateLiveData
    fun getPlayerStatusLiveData(): LiveData<PlayerStatus> = playerStatusLiveData

    fun playback() {
        when (playerStatusLiveData.value) {
            is PlayerStatus.Playing -> {
                playerStatusLiveData.value = PlayerStatus.Paused
                player.pause()
            }
            is PlayerStatus.Paused, is PlayerStatus.Prepared -> {
                playerStatusLiveData.value = PlayerStatus.Playing(getLength(player.getCurrentPosition()))
                player.play()
                mainHandler.postDelayed(refreshPosition, DELAY_MILLIS)
            }
            else -> { }
        }
    }

    fun pause() {
        playerStatusLiveData.value = PlayerStatus.Paused
        player.pause()
    }

    override fun onCleared() {
        super.onCleared()
        playerStatusLiveData.value = PlayerStatus.Default
        player.releaseResources()
    }

    private fun getLength(time: Int): String = SimpleDateFormat("mm:ss", Locale.getDefault()).format(time)

    companion object {
        private const val DELAY_MILLIS = 333L

        fun getViewModelFactory(): ViewModelProvider.Factory {
            return viewModelFactory {
                initializer {
                    val application = this[APPLICATION_KEY] as App
                    val context = application.applicationContext
                    val getTrackUseCase = Creator.createGetTrackUseCase(context)
                    AudiolayerViewModel(getTrackUseCase, application)
                }
            }
        }
    }
}