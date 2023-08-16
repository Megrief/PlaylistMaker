package com.example.playlistmaker.app.di

import com.example.playlistmaker.domain.entities.Track
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

    factory<GetDataUseCase<Track?>>(named(GET_TRACK_USE_CASE)) {
        val storageManagerRepo: StorageManagerRepo<Track?> = get(named(STORAGE_MANAGER_REPO_TRACK))
        GetTrackUseCase(repository = storageManagerRepo)
    }

    factory<GetDataUseCase<List<Track>>>(named(GET_TRACK_LIST_USE_CASE)) {
        val storageManagerRepo: StorageManagerRepo<List<Track>> = get(named(
            STORAGE_MANAGER_REPO_LIST))
        GetTrackListUseCase(repository = storageManagerRepo)
    }

    factory<StoreDataUseCase<List<Track>>>(named(STORE_TRACK_LIST_USE_CASE)) {
        val storageManagerRepo: StorageManagerRepo<List<Track>> = get(named(
            STORAGE_MANAGER_REPO_LIST))
        StoreTrackListUseCase(repository = storageManagerRepo)
    }

    factory<StoreDataUseCase<Track>>(named(STORE_TRACK_USE_CASE)) {
        val storageManagerRepo: StorageManagerRepo<Track?> = get(named(STORAGE_MANAGER_REPO_TRACK))
        StoreTrackUseCase(repository = storageManagerRepo)
    }

}