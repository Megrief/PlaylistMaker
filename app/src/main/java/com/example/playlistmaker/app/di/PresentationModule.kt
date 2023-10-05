package com.example.playlistmaker.app.di

import android.media.MediaPlayer
import com.example.playlistmaker.domain.entity.Track
import com.example.playlistmaker.domain.media.entity.Playlist
import com.example.playlistmaker.domain.settings.entity.ThemeFlag
import com.example.playlistmaker.domain.storage.use_cases.GetDataUseCase
import com.example.playlistmaker.domain.storage.use_cases.StoreDataUseCase
import com.example.playlistmaker.ui.audioplayer.view_model.AudioplayerViewModel
import com.example.playlistmaker.ui.audioplayer.view_model.player.Player
import com.example.playlistmaker.ui.media.fragments.view_models.PlaylistsViewModel
import com.example.playlistmaker.ui.media.fragments.view_models.SavedMediaViewModel
import com.example.playlistmaker.ui.search.view_model.SearchViewModel
import com.example.playlistmaker.ui.settings.utils.SingleLiveEvent
import com.example.playlistmaker.ui.settings.view_model.DarkThemeState
import com.example.playlistmaker.ui.settings.view_model.SettingsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

val presentationModule = module {

    viewModel {
        val getTrackUseCase: GetDataUseCase<Track?> = get(named(GET_TRACK_USE_CASE))
        AudioplayerViewModel(
            getDataUseCase =  getTrackUseCase,
            player = get()
        )
    }

    viewModel {
        val searchUseCase: GetDataUseCase<List<Track>?> = get(named(SEARCH_USE_CASE))
        val storeTrackUseCase: StoreDataUseCase<Track> = get(named(STORE_TRACK_USE_CASE))
        val storeTrackListUseCase: StoreDataUseCase<List<Track>> = get(named(STORE_TRACK_LIST_USE_CASE))
        val getTrackListUseCase: GetDataUseCase<List<Track>> = get(named(GET_TRACK_LIST_USE_CASE))
        SearchViewModel(
            searchUseCase = searchUseCase,
            storeTrackUseCase = storeTrackUseCase,
            storeTrackListUseCase = storeTrackListUseCase,
            getTrackListUseCase = getTrackListUseCase
        )
    }

    viewModel {
        val getThemeFlagUseCase: GetDataUseCase<ThemeFlag?> = get(named(GET_THEME_FLAG_USE_CASE))
        SettingsViewModel(
            getThemeFlagUseCase = getThemeFlagUseCase,
            switchThemeUseCase = get(),
            sharingRepository = get(),
            darkThemeState = get()
        )
    }

    viewModel {
        val getPlaylistListUseCase: GetDataUseCase<List<Playlist>> = get(named(GET_PLAYLIST_LIST_USE_CASE))
        val storePlaylistListUseCase: StoreDataUseCase<List<Playlist>> = get(named(STORE_PLAYLIST_LIST_USE_CASE))
        val storePlaylistUseCase: StoreDataUseCase<Playlist> = get(named(STORE_PLAYLIST_USE_CASE))

        PlaylistsViewModel(
            getPlaylists = getPlaylistListUseCase,
            storePlaylists = storePlaylistListUseCase,
            storePlaylist = storePlaylistUseCase
        )
    }

    viewModel {
        val getTrackListUseCase: GetDataUseCase<List<Track>> = get(named(GET_TRACK_LIST_USE_CASE))
        val storeTrackUseCase: StoreDataUseCase<Track> = get(named(STORE_TRACK_USE_CASE))
        val storeTrackListUseCase: StoreDataUseCase<List<Track>> = get(named(STORE_TRACK_LIST_USE_CASE))

        SavedMediaViewModel(
            getTrackListUseCase = getTrackListUseCase,
            storeTrackListUseCase = storeTrackListUseCase,
            storeTrackUseCase = storeTrackUseCase
        )
    }

    factory {
        SingleLiveEvent<DarkThemeState>()
    }

    factory {
        Player(player = get())
    }

    factory {
        MediaPlayer()
    }


}