package com.example.playlistmaker.app.di

import com.example.playlistmaker.domain.db.use_cases.DeleteItemUseCase
import com.example.playlistmaker.domain.db.use_cases.GetItemByIdUseCase
import com.example.playlistmaker.domain.db.use_cases_impl.DeleteTrackUseCaseImpl
import com.example.playlistmaker.domain.db.use_cases_impl.GetFavouritesUseCaseImpl
import com.example.playlistmaker.domain.db.use_cases_impl.GetTrackByIdUseCaseImpl
import com.example.playlistmaker.domain.db.use_cases_impl.StoreTrackInDbUseCaseImpl
import com.example.playlistmaker.domain.entity.Track
import com.example.playlistmaker.domain.search.SearchUseCase
import com.example.playlistmaker.domain.search.use_cases_impl.SearchUseCaseImpl
import com.example.playlistmaker.domain.settings.SettingsRepository
import com.example.playlistmaker.domain.settings.entity.ThemeFlag
import com.example.playlistmaker.domain.settings.use_cases_impl.GetThemeFlagUseCase
import com.example.playlistmaker.domain.settings.use_cases_impl.StoreThemeFlagUseCase
import com.example.playlistmaker.domain.settings.use_cases_impl.SwitchThemeUseCase
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
const val DELETE_TRACK_USE_CASE = "DeleteTrackUseCase"
const val GET_TRACK_BY_ID_USE_CASE = "GetTrackByIdUseCase"
const val GET_FAVOURITES_USE_CASE = "GetFavouritesUseCase"
const val STORE_TRACK_IN_DB_USE_CASE = "StoreTrackInDbUseCase"
val domainModule = module {

    factory<DeleteItemUseCase<Track>>(named(DELETE_TRACK_USE_CASE)) {
        DeleteTrackUseCaseImpl(
            repository = get(named(DB_REPO_TRACK))
        )
    }

    factory<GetItemByIdUseCase<Track>>(named(GET_TRACK_BY_ID_USE_CASE)) {
        GetTrackByIdUseCaseImpl(
            repository = get(named(DB_REPO_TRACK))
        )
    }

    factory<GetDataUseCase<List<Track>>>(named(GET_FAVOURITES_USE_CASE)) {
        GetFavouritesUseCaseImpl(
            repository = get(named(DB_REPO_TRACK))
        )
    }

    factory<StoreDataUseCase<Track>>(named(STORE_TRACK_IN_DB_USE_CASE)) {
        StoreTrackInDbUseCaseImpl(
            repository = get(named(DB_REPO_TRACK))
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