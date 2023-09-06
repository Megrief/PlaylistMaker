package com.example.playlistmaker.ui.audioplayer.view_model

import android.icu.text.SimpleDateFormat
import android.os.Handler
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.domain.entity.Track
import com.example.playlistmaker.domain.storage.use_cases.GetDataUseCase
import com.example.playlistmaker.ui.audioplayer.view_model.player.Player
import com.example.playlistmaker.ui.audioplayer.view_model.player.PlayerStatus
import com.example.playlistmaker.ui.search.view_model.SearchViewModel
import org.koin.java.KoinJavaComponent.getKoin
import java.util.Locale

class AudioplayerViewModel(
    getDataUseCase: GetDataUseCase<Track?>,
    private val player: Player
) : ViewModel() {

    private val screenStateLiveData = MutableLiveData<AudioplayerScreenState>(AudioplayerScreenState.Loading)
    private val playerStatusLiveData = MutableLiveData<PlayerStatus>(PlayerStatus.Default)
    private val mainHandler: Handler = getKoin().get()

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
        getDataUseCase.get(SearchViewModel.TRACK) { track ->
            screenStateLiveData.postValue(
                if (track != null) {
                    if (track.previewUrl.isNotBlank()) {
                        player.configurePlayer(
                            track.previewUrl,
                            { playerStatusLiveData.value = PlayerStatus.Prepared },
                            { playerStatusLiveData.value = PlayerStatus.Prepared }
                        )
                    }
                    AudioplayerScreenState.Content(track)
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

    }
}