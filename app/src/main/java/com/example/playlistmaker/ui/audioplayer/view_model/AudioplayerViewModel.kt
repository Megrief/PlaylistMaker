package com.example.playlistmaker.ui.audioplayer.view_model

import android.icu.text.SimpleDateFormat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.entity.Track
import com.example.playlistmaker.domain.storage.use_cases.GetDataUseCase
import com.example.playlistmaker.ui.audioplayer.view_model.player.Player
import com.example.playlistmaker.ui.audioplayer.view_model.player.PlayerStatus
import com.example.playlistmaker.ui.search.view_model.SearchViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.util.Locale

class AudioplayerViewModel(
    getDataUseCase: GetDataUseCase<Track?>,
    private val player: Player
) : ViewModel() {

    private val _screenState = MutableLiveData<AudioplayerScreenState>(AudioplayerScreenState.Loading)
    val screenState: LiveData<AudioplayerScreenState>
        get() = _screenState

    private val _playerStatus = MutableLiveData<PlayerStatus>(PlayerStatus.Default)
    val playerStatus: LiveData<PlayerStatus>
        get() = _playerStatus

    private var playingProcess: Job? = null

    init {
        getDataUseCase.get(SearchViewModel.TRACK) { track ->
            _screenState.postValue(
                if (track != null) {
                    if (track.previewUrl.isNotBlank()) {
                        player.configurePlayer(
                            track.previewUrl,
                            onPrepared = { _playerStatus.value = PlayerStatus.Prepared },
                            onCompleted = {
                                playingProcess?.cancel()
                                playingProcess = null
                                _playerStatus.value = (playerStatus.value as PlayerStatus.Playing)
                                    .copy(currentPosition = getLength())
                                _playerStatus.value = PlayerStatus.Prepared
                            }
                        )
                    }
                    AudioplayerScreenState.Content(track)
                } else AudioplayerScreenState.Error
            )
        }
    }

    fun playback() {
        when (_playerStatus.value) {
            is PlayerStatus.Playing -> {
                _playerStatus.value = PlayerStatus.Paused
                playingProcess?.cancel()
                playingProcess = null
                player.pause()
            }
            is PlayerStatus.Paused, is PlayerStatus.Prepared -> {
                _playerStatus.value = PlayerStatus.Playing(getLength(player.currentPosition))
                player.play()
                playingProcess = getPosition()
            }
            else -> { }
        }
    }

    fun pause() {
        _playerStatus.value = PlayerStatus.Paused
        player.pause()
    }

    override fun onCleared() {
        super.onCleared()
        _playerStatus.value = PlayerStatus.Default
        player.releaseResources()
    }

    private fun getLength(time: Int = 0): String = SimpleDateFormat("mm:ss", Locale.getDefault()).format(time)

    private fun getPosition(): Job = viewModelScope.launch {
        player.getCurrentPosition().collect { position ->
            val value = playerStatus.value
            if (value is PlayerStatus.Playing) {
                _playerStatus.value = value.copy(
                    currentPosition = getLength(position)
                )
            } else _playerStatus.value = PlayerStatus.Playing(getLength(position))
        }
    }
}