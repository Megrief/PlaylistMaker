package com.example.playlistmaker.ui.audioplayer.view_model

import android.icu.text.SimpleDateFormat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.entities.Playlist
import com.example.playlistmaker.domain.entities.Track
import com.example.playlistmaker.domain.storage.db.use_cases.DeleteItemUseCase
import com.example.playlistmaker.domain.storage.db.use_cases.GetItemByIdUseCase
import com.example.playlistmaker.domain.storage.use_cases.GetDataUseCase
import com.example.playlistmaker.domain.storage.use_cases.StoreDataUseCase
import com.example.playlistmaker.ui.audioplayer.view_model.player.Player
import com.example.playlistmaker.ui.audioplayer.view_model.player.PlayerStatus
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Locale

class AudioplayerViewModel(
    getDataUseCase: GetDataUseCase<Track?>,
    private val getItemByIdUseCase: GetItemByIdUseCase<Track>,
    private val deleteItemUseCase: DeleteItemUseCase,
    private val storeDataUseCase: StoreDataUseCase<Track>,
    private val storePlaylist: StoreDataUseCase<Playlist>,
    private val getPlaylists: GetDataUseCase<List<Playlist>>,
    private val player: Player
) : ViewModel() {

    private val _screenState = MutableLiveData<AudioplayerScreenState>(AudioplayerScreenState.Loading)
    val screenState: LiveData<AudioplayerScreenState>
        get() = _screenState

    private val _playerStatus = MutableLiveData<PlayerStatus>(PlayerStatus.Default)
    val playerStatus: LiveData<PlayerStatus>
        get() = _playerStatus

    private var playingTimeCounter: Job? = null
        set(value) {
            field?.cancel()
            field = value
        }

    init {
        viewModelScope.launch(Dispatchers.IO) {
            getDataUseCase.get().single().let { track ->
                _screenState.postValue(
                    if (track != null) {
                        player.configurePlayer(
                            track.previewUrl,
                            onPrepared = {
                                _playerStatus.value = PlayerStatus.Prepared(null)
                            },
                            onCompleted = {
                                playingTimeCounter = null
                                _playerStatus.value = PlayerStatus.Prepared(getLength())
                            }
                        )
                        AudioplayerScreenState.Content(track, inFavourite(track.id), getPlaylists.get().single())
                    } else AudioplayerScreenState.Error
                )
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        _playerStatus.value = PlayerStatus.Default
        player.releaseResources()
        playingTimeCounter = null
    }

    fun playback() {
        when (_playerStatus.value) {
            is PlayerStatus.Playing -> {
                _playerStatus.value = PlayerStatus.Paused
                playingTimeCounter = null
                player.pause()
            }
            is PlayerStatus.Paused, is PlayerStatus.Prepared -> {
                player.play()
                playingTimeCounter = getPosition()
            }
            else -> { }
        }
    }

    fun stop() {
        if (playerStatus.value is PlayerStatus.Playing) {
            playback()
        }
    }

    fun likeback() {
        viewModelScope.launch(Dispatchers.IO) {
            with(_screenState.value as AudioplayerScreenState.Content) {
                if (inFavourite)
                    deleteItemUseCase.delete(track.id)
                else
                    storeDataUseCase.store(track)
                withContext(Dispatchers.Main) {
                    _screenState.value = copy(inFavourite = !inFavourite)
                }
            }
        }
    }

    fun addToPlaylist(playlist: Playlist, onAddingCallback: () -> Unit, onErrorCallback: () -> Unit) {
        val content: AudioplayerScreenState.Content? = (screenState.value as? AudioplayerScreenState.Content)
        val track = content?.track
        track?.run {
            if (playlist.trackIdsList.contains(id)) {
                onErrorCallback()
            } else {
                playlist.trackIdsList.add(id)
                viewModelScope.launch(Dispatchers.IO) {
                    storePlaylist.store(playlist)
                    _screenState.postValue(
                        content.copy(playlists = getPlaylists.get().single())
                    )
                }
                onAddingCallback()
            }
        }
    }

    suspend fun getPlaylists(): Flow<List<Playlist>> = getPlaylists.get()

    private suspend fun inFavourite(id: Long): Boolean = getItemByIdUseCase.get(id).single() != null

    private fun getLength(time: Int = 0): String = SimpleDateFormat("mm:ss", Locale.getDefault()).format(time)

    private fun getPosition(): Job = viewModelScope.launch {
        player.getCurrentPosition().collect { position ->
            val value = playerStatus.value
            if (value is PlayerStatus.Playing)
                _playerStatus.value = value.copy(getLength(position))
            else
                _playerStatus.value = PlayerStatus.Playing(getLength(position))
        }
    }
}