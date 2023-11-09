package com.example.playlistmaker.app.di

import android.media.MediaPlayer
import com.example.playlistmaker.ui.audioplayer.view_model.AudioplayerViewModel
import com.example.playlistmaker.ui.audioplayer.view_model.player.Player
import com.example.playlistmaker.ui.media.fragments.view_models.FavoritesViewModel
import com.example.playlistmaker.ui.media.fragments.view_models.PlaylistsViewModel
import com.example.playlistmaker.ui.playlist_creation.view_model.PlaylistCreationViewModel
import com.example.playlistmaker.ui.playlist_page.view_model.PlaylistPageViewModel
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
            getItemUseCase = get(named(GET_TRACK_USE_CASE)),
            getItemByIdUseCase = get(named(GET_TRACK_BY_ID_USE_CASE)),
            deleteItemUseCase = get(named(DELETE_TRACK_USE_CASE)),
            storeItemUseCase = get(named(STORE_TRACK_IN_DB_USE_CASE)),
            storePlaylist = get(named(STORE_PLAYLIST_IN_DB_USE_CASE)),
            getPlaylists = get(named(GET_PLAYLISTS_USE_CASE)),
            storeTrackInPlaylistDb = get(named(STORE_TRACK_IN_PLAYLIST_DB)),
            getTrackInPlaylistById = get(named(GET_TRACK_IN_PLAYLIST_BY_ID)),
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
            storePhotoUseCaseImpl = get(named(STORE_PHOTO_USE_CASE)),
            storePlaylistInDb = get(named(STORE_PLAYLIST_IN_DB_USE_CASE)),
            getPhotoByIdUseCase = get(named(GET_PHOTO_BY_ID_USE_CASE)),
            getPhotoIdUseCase = get(named(GET_ID_USE_CASE))
        )
    }

    viewModel {
        PlaylistsViewModel(
            getPlaylistsUseCaseImpl = get(named(GET_PLAYLISTS_USE_CASE)),
            storePlaylistsIdUseCase = get(named(STORE_ID_USE_CASE))
        )
    }

    viewModel {
        PlaylistPageViewModel(
            getPlaylistsIdUseCase = get(named(GET_ID_USE_CASE)),
            getPlaylistByIdUseCase = get(named(GET_PLAYLIST_BY_ID_USE_CASE)),
            storeTrackUseCase = get(named(STORE_TRACK_USE_CASE)),
            getTrackByIdUseCase = get(named(GET_TRACK_BY_ID_USE_CASE)),
            getPhotoByIdUseCase = get(named(GET_PHOTO_BY_ID_USE_CASE)),
            deleteTrackUseCase = get(named(DELETE_TRACK_IN_PLAYLIST)),
            getPlaylistsUseCase = get(named(GET_PLAYLISTS_USE_CASE)),
            storePlaylistUseCase = get(named(STORE_PLAYLIST_IN_DB_USE_CASE))
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