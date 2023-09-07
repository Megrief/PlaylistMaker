package com.example.playlistmaker.app.di

import com.example.playlistmaker.domain.entity.Track
import com.example.playlistmaker.domain.media.entity.Playlist
import com.example.playlistmaker.domain.media.use_cases_impl.GetPlaylistListUseCase
import com.example.playlistmaker.domain.media.use_cases_impl.GetPlaylistUseCase
import com.example.playlistmaker.domain.media.use_cases_impl.StorePlaylistListUseCase
import com.example.playlistmaker.domain.media.use_cases_impl.StorePlaylistUseCase
import com.example.playlistmaker.domain.search.use_cases_impl.SearchUseCaseImpl
import com.example.playlistmaker.domain.settings.SettingsRepository
import com.example.playlistmaker.domain.settings.entity.ThemeFlag
import com.example.playlistmaker.domain.settings.use_cases_impl.GetThemeFlagUseCase
import com.example.playlistmaker.domain.settings.use_cases_impl.StoreThemeFlagUseCase
import com.example.playlistmaker.domain.settings.use_cases_impl.SwitchThemeUseCase
import com.example.playlistmaker.domain.storage.StorageManagerRepo
import com.example.playlistmaker.domain.storage.use_cases.GetDataUseCase
import com.example.playlistmaker.domain.storage.use_cases.StoreDataUseCase
import com.example.playlistmaker.domain.storage.use_cases_impl.GetTrackListUseCase
import com.example.playlistmaker.domain.storage.use_cases_impl.GetTrackUseCase
import com.example.playlistmaker.domain.storage.use_cases_impl.StoreTrackListUseCase
import com.example.playlistmaker.domain.storage.use_cases_impl.StoreTrackUseCase
import org.koin.core.qualifier.named
import org.koin.dsl.module

const val SEARCH_USE_CASE = "SearchUseCase"
const val GET_THEME_FLAG_USE_CASE = "GetThemeFlagUseCase"
const val STORE_THEME_FLAG_USE_CASE = "StoreThemeFlagUseCase"
const val GET_TRACK_USE_CASE = "GetTrackUseCase"
const val GET_TRACK_LIST_USE_CASE = "GetTrackListUseCase"
const val STORE_TRACK_LIST_USE_CASE = "StoreTrackListUseCase"
const val STORE_TRACK_USE_CASE = "StoreTrackUseCase"
const val STORE_PLAYLIST_USE_CASE = "StorePlaylistUseCase"
const val GET_PLAYLIST_USE_CASE = "GetPlaylistUseCase"
const val GET_PLAYLIST_LIST_USE_CASE = "GetPlaylistListUseCase"
const val STORE_PLAYLIST_LIST_USE_CASE = "StorePlaylistListUseCase"

val domainModule = module {
    factory<GetDataUseCase<List<Track>?>>(named(SEARCH_USE_CASE)) {
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
        val storageManagerRepo: StorageManagerRepo<Track?> = get(named(STORAGE_MANAGER_REPO_TRACK))
        StoreTrackUseCase(repository = storageManagerRepo)
    }

    factory<GetDataUseCase<Track?>>(named(GET_TRACK_USE_CASE)) {
        val storageManagerRepo: StorageManagerRepo<Track?> = get(named(STORAGE_MANAGER_REPO_TRACK))
        GetTrackUseCase(repository = storageManagerRepo)
    }

    factory<StoreDataUseCase<List<Track>>>(named(STORE_TRACK_LIST_USE_CASE)) {
        val storageManagerRepo: StorageManagerRepo<List<Track>> = get(named(STORAGE_MANAGER_REPO_LIST))
        StoreTrackListUseCase(repository = storageManagerRepo)
    }

    factory<GetDataUseCase<List<Track>>>(named(GET_TRACK_LIST_USE_CASE)) {
        val storageManagerRepo: StorageManagerRepo<List<Track>> = get(named(STORAGE_MANAGER_REPO_LIST))
        GetTrackListUseCase(repository = storageManagerRepo)
    }

    factory<StoreDataUseCase<Playlist>>(named(STORE_PLAYLIST_USE_CASE)) {
        val storageManagerRepo: StorageManagerRepo<Playlist?> = get(named(STORAGE_MANAGER_REPO_PLAYLIST))
        StorePlaylistUseCase(repository = storageManagerRepo)
    }

    factory<GetDataUseCase<Playlist?>>(named(GET_PLAYLIST_USE_CASE)) {
        val storageManagerRepo: StorageManagerRepo<Playlist?> = get(named(STORAGE_MANAGER_REPO_PLAYLIST))
        GetPlaylistUseCase(repository = storageManagerRepo)
    }

    factory<StoreDataUseCase<List<Playlist>>>(named(STORE_PLAYLIST_LIST_USE_CASE)) {
        val storageManagerRepo: StorageManagerRepo<List<Playlist>> = get(named(STORAGE_MANAGER_REPO_PLAYLIST_LIST))
        StorePlaylistListUseCase(repository = storageManagerRepo)
    }

    factory<GetDataUseCase<List<Playlist>>>(named(GET_PLAYLIST_LIST_USE_CASE)) {
        val storageRepository: StorageManagerRepo<List<Playlist>> = get(named(STORAGE_MANAGER_REPO_PLAYLIST_LIST))
        GetPlaylistListUseCase(repository = storageRepository)
    }

}