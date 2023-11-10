package com.example.playlistmaker.ui.playlist_page.view_model

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.entities.Playlist
import com.example.playlistmaker.domain.entities.Track
import com.example.playlistmaker.domain.sharing.use_cases.ShareStringUseCase
import com.example.playlistmaker.domain.storage.use_cases.DeleteItemUseCase
import com.example.playlistmaker.domain.storage.use_cases.GetItemByIdUseCase
import com.example.playlistmaker.domain.storage.use_cases.GetItemUseCase
import com.example.playlistmaker.domain.storage.use_cases.StoreItemUseCase
import com.example.playlistmaker.ui.playlist_page.screen_state.PlaylistPageScreenState
import com.example.playlistmaker.utils.getCorrectTime
import com.example.playlistmaker.utils.getCorrectTracks
import com.example.playlistmaker.utils.getLength
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.flow.singleOrNull
import kotlinx.coroutines.launch

class PlaylistPageViewModel(
    private val getTrackByIdUseCase: GetItemByIdUseCase<Track>,
    getPlaylistByIdUseCase: GetItemByIdUseCase<Playlist>,
    getPlaylistsIdUseCase: GetItemUseCase<Long>,
    private val storeTrackUseCase: StoreItemUseCase<Track>,
    private val getPhotoByIdUseCase: GetItemByIdUseCase<Uri>,
    private val deleteTrackUseCase: DeleteItemUseCase<Track>,
    private val getPlaylistsUseCase: GetItemUseCase<List<Playlist>>,
    private val storePlaylistUseCase: StoreItemUseCase<Playlist>,
    private val sharePlaylistUseCase: ShareStringUseCase
) : ViewModel() {

    private val _screenState: MutableLiveData<PlaylistPageScreenState> = MutableLiveData()
    val screenState: LiveData<PlaylistPageScreenState>
        get() = _screenState

    init {
        viewModelScope.launch(Dispatchers.IO) {
            getPlaylistsIdUseCase.get().singleOrNull()?.also { playlistId ->
                getPlaylistByIdUseCase.get(playlistId).singleOrNull()?.run {
                    getState(this)
                }
            }
        }
    }

    suspend fun getPhoto(id: Long): Uri? = getPhotoByIdUseCase.get(id).singleOrNull()

    fun deleteTrack(track: Track) {
        viewModelScope.launch(Dispatchers.IO) {
            (screenState.value as? PlaylistPageScreenState.Content)?.run {
                playlist.trackIdsList.remove(track.id)
                getState(playlist)
                storePlaylistUseCase.store(playlist)
                if (!trackInOtherList(track.id)) deleteTrackUseCase.delete(track)
            }
        }
    }

    fun sharePlaylist() {
        viewModelScope.launch(Dispatchers.IO) {
            sharePlaylistUseCase.share(buildPlaylistString())
        }
    }

    fun storeTrack(track: Track) {
        viewModelScope.launch(Dispatchers.IO) {
            storeTrackUseCase.store(track)
        }
    }

    private fun buildPlaylistString(): String = buildString {
        (screenState.value as? PlaylistPageScreenState.Content)?.run {
            append("${ playlist.name }\n")
            append("${ playlist.description }\n")
            append("$totalTracks\n")
            trackList.forEachIndexed { index, track ->
                append("${ index + 1 }. ${ track.artistName } - ${ track.trackName } (${ getLength(track.trackTime) })\n")
            }
        }
    }

    private suspend fun getState(playlist: Playlist) {
        val trackList = playlist.trackIdsList
            .mapNotNull { getTrackByIdUseCase.get(it).singleOrNull() }
        val totalLength = getCorrectTime(trackList.sumOf { it.trackTime })
        val totalTracks = "${ trackList.size } ${ getCorrectTracks(playlist.trackIdsList.size.toString()) }"
        _screenState.postValue(
            PlaylistPageScreenState.Content(
                playlist = playlist,
                trackList = trackList,
                totalLength = totalLength,
                totalTracks = totalTracks
            )
        )
    }

    private suspend fun trackInOtherList(trackId: Long): Boolean =
        getPlaylistsUseCase.get().single()?.any { it.trackIdsList.contains(trackId) } ?: false

}