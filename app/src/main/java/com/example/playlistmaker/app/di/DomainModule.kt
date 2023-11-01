package com.example.playlistmaker.app.di

import com.example.playlistmaker.domain.entity.Track
import com.example.playlistmaker.domain.media.entity.Playlist
import com.example.playlistmaker.domain.media.favorites.use_cases_impl.DeleteTrackUseCaseImpl
import com.example.playlistmaker.domain.media.favorites.use_cases_impl.GetFavoritesUseCaseImpl
import com.example.playlistmaker.domain.media.favorites.use_cases_impl.GetTrackByIdUseCaseImpl
import com.example.playlistmaker.domain.media.favorites.use_cases_impl.StoreTrackInDbUseCaseImpl
import com.example.playlistmaker.domain.media.playlists.use_cases_impl.GetPhotoByNameUseCase
import com.example.playlistmaker.domain.media.playlists.use_cases_impl.GetPlaylistsUseCaseImpl
import com.example.playlistmaker.domain.media.playlists.use_cases_impl.StorePhotoUseCase
import com.example.playlistmaker.domain.media.playlists.use_cases_impl.StorePlaylistInDbUseCaseImpl
import com.example.playlistmaker.domain.search.SearchUseCase
import com.example.playlistmaker.domain.search.use_cases_impl.SearchUseCaseImpl
import com.example.playlistmaker.domain.settings.SettingsRepository
import com.example.playlistmaker.domain.settings.entity.ThemeFlag
import com.example.playlistmaker.domain.settings.use_cases_impl.GetThemeFlagUseCase
import com.example.playlistmaker.domain.settings.use_cases_impl.StoreThemeFlagUseCase
import com.example.playlistmaker.domain.settings.use_cases_impl.SwitchThemeUseCase
import com.example.playlistmaker.domain.storage.db.DbRepo
import com.example.playlistmaker.domain.storage.db.use_cases.DeleteItemUseCase
import com.example.playlistmaker.domain.storage.db.use_cases.GetItemByIdUseCase
import com.example.playlistmaker.domain.storage.shared_prefs_use_cases.GetTrackListUseCase
import com.example.playlistmaker.domain.storage.shared_prefs_use_cases.GetTrackUseCase
import com.example.playlistmaker.domain.storage.shared_prefs_use_cases.StoreTrackListUseCase
import com.example.playlistmaker.domain.storage.shared_prefs_use_cases.StoreTrackUseCase
import com.example.playlistmaker.domain.storage.use_cases.GetDataUseCase
import com.example.playlistmaker.domain.storage.use_cases.StoreDataUseCase
import org.koin.core.qualifier.named
import org.koin.dsl.module

const val SEARCH_USE_CASE = "SearchUseCase"
const val GET_THEME_FLAG_USE_CASE = "GetThemeFlagUseCase"
const val STORE_THEME_FLAG_USE_CASE = "StoreThemeFlagUseCase"
const val GET_TRACK_USE_CASE = "GetTrackUseCase"
const val GET_TRACK_LIST_USE_CASE = "GetTrackListUseCase"
const val STORE_TRACK_LIST_USE_CASE = "StoreTrackListUseCase"
const val STORE_TRACK_USE_CASE = "StoreTrackUseCase"
const val DELETE_TRACK_USE_CASE = "DeleteTrackUseCase"
const val GET_TRACK_BY_ID_USE_CASE = "GetTrackByIdUseCase"
const val GET_FAVOURITES_USE_CASE = "GetFavouritesUseCase"
const val STORE_TRACK_IN_DB_USE_CASE = "StoreTrackInDbUseCase"
const val STORE_PLAYLIST_IN_DB_USE_CASE = "StorePlaylistInDbUseCase"
const val GET_PLAYLISTS_USE_CASE = "GetPlaylistsUseCase"
const val DELETE_PLAYLIST_FROM_DB_USE_CASE = "DeletePlaylistFromDbUseCase"

val domainModule = module {

    factory {
        GetPhotoByNameUseCase(
            repository = get(named(EXTERNAL_STORAGE_REPO_PHOTO))
        )
    }

    factory {
        StorePhotoUseCase(
            repository = get(named(EXTERNAL_STORAGE_REPO_PHOTO))
        )
    }

    factory<DeleteItemUseCase>(named(DELETE_TRACK_USE_CASE)) {
        DeleteTrackUseCaseImpl(
            repository = get(named(DB_REPO_TRACK))
        )
    }

    factory<GetItemByIdUseCase<Track>>(named(GET_TRACK_BY_ID_USE_CASE)) {
        GetTrackByIdUseCaseImpl(
            repository = get(named(DB_REPO_TRACK))
        )
    }

    factory<GetDataUseCase<List<Playlist>>>(named(GET_PLAYLISTS_USE_CASE)) {
        val repository: DbRepo<Playlist> = get(named(DB_REPO_PLAYLIST))
        GetPlaylistsUseCaseImpl(
            repository = repository
        )
    }
    factory<GetDataUseCase<List<Track>>>(named(GET_FAVOURITES_USE_CASE)) {
        val repository: DbRepo<Track> = get(named(DB_REPO_TRACK))
        GetFavoritesUseCaseImpl(
            repository = repository
        )
    }

    factory<StoreDataUseCase<Playlist>>(named(STORE_PLAYLIST_IN_DB_USE_CASE)) {
        val repository: DbRepo<Playlist> = get(named(DB_REPO_PLAYLIST))
        StorePlaylistInDbUseCaseImpl(
            repository = repository
        )
    }

    factory<StoreDataUseCase<Track>>(named(STORE_TRACK_IN_DB_USE_CASE)) {
        val repository: DbRepo<Track> = get(named(DB_REPO_TRACK))
        StoreTrackInDbUseCaseImpl(
            repository = repository
        )
    }

    factory<SearchUseCase>(named(SEARCH_USE_CASE)) {
        SearchUseCaseImpl(repository = get())
    }

    factory<GetDataUseCase<ThemeFlag?>>(named(GET_THEME_FLAG_USE_CASE)) {
        val storageManager: SettingsRepository = get()
        GetThemeFlagUseCase(repository = storageManager)
    }

    factory<StoreDataUseCase<ThemeFlag>>(named(STORE_THEME_FLAG_USE_CASE)) {
        val storageManager: SettingsRepository = get()
        StoreThemeFlagUseCase(repository = storageManager)
    }

    factory {
        SwitchThemeUseCase(repository = get())
    }

    factory<StoreDataUseCase<Track>>(named(STORE_TRACK_USE_CASE)) {
        StoreTrackUseCase(repository = get(named(STORAGE_MANAGER_REPO_TRACK)))
    }

    factory<GetDataUseCase<Track?>>(named(GET_TRACK_USE_CASE)) {
        GetTrackUseCase(repository = get(named(STORAGE_MANAGER_REPO_TRACK)))
    }

    factory<StoreDataUseCase<List<Track>>>(named(STORE_TRACK_LIST_USE_CASE)) {
        StoreTrackListUseCase(repository = get(named(STORAGE_MANAGER_REPO_LIST)))
    }

    factory<GetDataUseCase<List<Track>>>(named(GET_TRACK_LIST_USE_CASE)) {
        GetTrackListUseCase(repository = get(named(STORAGE_MANAGER_REPO_LIST)))
    }

}