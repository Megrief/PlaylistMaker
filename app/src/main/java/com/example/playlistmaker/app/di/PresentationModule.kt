package com.example.playlistmaker.app.di

import android.media.MediaPlayer
import com.example.playlistmaker.ui.audioplayer.view_model.AudioplayerViewModel
import com.example.playlistmaker.ui.audioplayer.view_model.player.Player
import com.example.playlistmaker.ui.media.fragments.view_models.FavoritesViewModel
import com.example.playlistmaker.ui.media.fragments.view_models.PlaylistsViewModel
import com.example.playlistmaker.ui.playlist_creation.view_model.PlaylistCreationViewModel
import com.example.playlistmaker.ui.search.view_model.SearchViewModel
import com.example.playlistmaker.utils.SingleLiveEvent
import com.example.playlistmaker.ui.settings.view_model.DarkThemeState
import com.example.playlistmaker.ui.settings.view_model.SettingsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

val presentationModule = module {

    viewModel {
        AudioplayerViewModel(
            getDataUseCase = get(named(GET_TRACK_USE_CASE)),
            getItemByIdUseCase = get(named(GET_TRACK_BY_ID_USE_CASE)),
            deleteItemUseCase = get(named(DELETE_TRACK_USE_CASE)),
            storeDataUseCase = get(named(STORE_TRACK_IN_DB_USE_CASE)),
            storePlaylist = get(named(STORE_PLAYLIST_IN_DB_USE_CASE)),
            getPlaylists = get(named(GET_PLAYLISTS_USE_CASE)),
            player = get()
        )
    }

    viewModel {
        SearchViewModel(
            searchUseCase = get(named(SEARCH_USE_CASE)),
            storeTrackUseCase = get(named(STORE_TRACK_USE_CASE)),
            storeTrackListUseCase = get(named(STORE_TRACK_LIST_USE_CASE)),
            getTrackListUseCase = get(named(GET_TRACK_LIST_USE_CASE))
        )
    }

    viewModel {
        SettingsViewModel(
            getThemeFlagUseCase = get(named(GET_THEME_FLAG_USE_CASE)),
            switchThemeUseCase = get(),
            sharingRepository = get(),
            darkThemeState = get()
        )
    }

    viewModel {
        FavoritesViewModel(
            getFavouritesUseCase = get(named(GET_FAVOURITES_USE_CASE)),
            storeTrackUseCase = get(named(STORE_TRACK_USE_CASE))
        )
    }

    viewModel {
        PlaylistCreationViewModel(
            storePhotoUseCase = get(),
            storePlaylistInDb = get(named(STORE_PLAYLIST_IN_DB_USE_CASE)),
        )
    }

    viewModel {
        PlaylistsViewModel(
            getPlaylistsUseCaseImpl = get(named(GET_PLAYLISTS_USE_CASE))
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