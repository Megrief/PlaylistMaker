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

val domainModule = module {
    factory<GetDataUseCase<List<Track>?>>(named("SearchUseCase")) {
        SearchUseCaseImpl(repository = get())
    }

    factory<GetDataUseCase<ThemeFlag?>>(named("GetThemeFlagUseCase")) {
        val storageManager: SettingsRepository = get()
        GetThemeFlagUseCase(repository = storageManager)
    }

    factory<StoreDataUseCase<ThemeFlag>>(named("StoreThemeFlagUseCase")) {
        val storageManager: SettingsRepository = get()
        StoreThemeFlagUseCase(repository = storageManager)
    }

    factory {
        SwitchThemeUseCase(repository = get())
    }

    factory<GetDataUseCase<Track?>>(named("GetTrackUseCase")) {
        val storageManagerRepo: StorageManagerRepo<Track?> = get(named("StorageManagerRepoTrack"))
        GetTrackUseCase(repository = storageManagerRepo)
    }

    factory<GetDataUseCase<List<Track>>>(named("GetTrackListUseCase")) {
        val storageManagerRepo: StorageManagerRepo<List<Track>> = get(named("StorageManagerRepoList"))
        GetTrackListUseCase(repository = storageManagerRepo)
    }

    factory<StoreDataUseCase<List<Track>>>(named("StoreTrackListUseCase")) {
        val storageManagerRepo: StorageManagerRepo<List<Track>> = get(named("StorageManagerRepoList"))
        StoreTrackListUseCase(repository = storageManagerRepo)
    }

    factory<StoreDataUseCase<Track>>(named("StoreTrackUseCase")) {
        val storageManagerRepo: StorageManagerRepo<Track?> = get(named("StorageManagerRepoTrack"))
        StoreTrackUseCase(repository = storageManagerRepo)
    }

}