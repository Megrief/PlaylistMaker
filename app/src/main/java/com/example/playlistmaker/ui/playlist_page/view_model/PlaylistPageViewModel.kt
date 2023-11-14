package com.example.playlistmaker.ui.playlist_page.view_model

import android.content.Context
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.R
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
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.flow.singleOrNull
import kotlinx.coroutines.launch

class PlaylistPageViewModel(
    private val getPlaylistByIdUseCase: GetItemByIdUseCase<Playlist>,
    private val getPlaylistsIdUseCase: GetItemUseCase<Long>,
    private val getTrackByIdUseCase: GetItemByIdUseCase<Track>,
    private val storeTrackUseCase: StoreItemUseCase<Track>,
    private val getPhotoByIdUseCase: GetItemByIdUseCase<Uri>,
    private val deleteTrackUseCase: DeleteItemUseCase<Track>,
    private val getPlaylistsUseCase: GetItemUseCase<List<Playlist>>,
    private val storePlaylistUseCase: StoreItemUseCase<Playlist>,
    private val sharePlaylistUseCase: ShareStringUseCase,
    private val deletePlaylistUseCase: DeleteItemUseCase<Playlist>,
    private val storePlaylistIdUseCase: StoreItemUseCase<Long>
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

    fun storePlaylistId() {
        viewModelScope.launch(Dispatchers.IO) {
            (screenState.value as? PlaylistPageScreenState.Content)?.run {
                storePlaylistIdUseCase.store(playlist?.id ?: 0L)
                // Not finished
            }
        }
    }

    fun checkState() {
        viewModelScope.launch(Dispatchers.IO) {
            val id = getPlaylistsIdUseCase.get().single()
            val newPlaylist = getPlaylistByIdUseCase.get(id ?: 0L).single()
            (screenState.value as? PlaylistPageScreenState.Content)?.run {
                if (newPlaylist != playlist) {
                    _screenState.postValue(
                        copy(playlist = newPlaylist)
                    )
                }
            }
        }
    }

    suspend fun getPhoto(id: Long): Uri? = getPhotoByIdUseCase.get(id).singleOrNull()

    fun sharePlaylist(): Boolean {
        val isEmpty = (screenState.value as? PlaylistPageScreenState.Content)?.trackList?.isEmpty() == true
        if (!isEmpty) {
            viewModelScope.launch(Dispatchers.IO) {
                sharePlaylistUseCase.share(buildPlaylistString())
            }
        }
        return isEmpty
    }

    fun storeTrack(track: Track) {
        viewModelScope.launch(Dispatchers.IO) {
            storeTrackUseCase.store(track)
        }
    }

    fun showOnDeleteTrackDialog(track: Track, context: Context) {
        MaterialAlertDialogBuilder(context)
            .setTitle(context.getString(R.string.delete_track))
            .setMessage(context.getString(R.string.really_delete))
            .setPositiveButton(context.getString(R.string.yes)) { _, _ ->
                viewModelScope.launch(Dispatchers.IO) {
                    deleteTrack(track)
                }
            }
            .setNegativeButton(context.getString(R.string.no)) { _, _ -> }
            .show()
    }

    fun showOnDeletePlaylistDialog(context: Context) {
        MaterialAlertDialogBuilder(context)
            .setTitle(context.getString(R.string.delete_playlist))
            .setMessage(context.getString(R.string.really_delete_playlist))
            .setPositiveButton(context.getString(R.string.yes)) { _, _ ->
                (screenState.value as? PlaylistPageScreenState.Content)?.run {
                    viewModelScope.launch(Dispatchers.IO) {
                        if (playlist != null) deletePlaylist(playlist)
                        _screenState.postValue(copy(playlist = null))
                    }
                }
            }
            .setNegativeButton(context.getString(R.string.no)) {_, _ -> }
            .show()
    }

    private suspend fun deleteTrack(track: Track) {
        (screenState.value as? PlaylistPageScreenState.Content)?.run {
            if (playlist != null) {
                playlist.trackIdsList.remove(track.id)
                getState(playlist)
                storePlaylistUseCase.store(playlist)
                if (!trackInOtherList(track.id)) deleteTrackUseCase.delete(track)
            }
        }
    }

    private suspend fun deletePlaylist(playlist: Playlist) {
        deletePlaylistUseCase.delete(playlist)
        playlist.trackIdsList.forEach {  trackId ->
            if (!trackInOtherList(trackId))
                getTrackByIdUseCase.get(trackId).single()?.run {
                    deleteTrackUseCase::delete
                }
        }
    }

    private fun buildPlaylistString(): String = buildString {
        (screenState.value as? PlaylistPageScreenState.Content)?.run {
            append("${ playlist?.name }\n")
            append("${ playlist?.description }\n")
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