package com.example.playlistmaker.utils.creator

import android.content.Context
import com.example.playlistmaker.data.search.SearchRepoImpl
import com.example.playlistmaker.data.search.network.network_client.impl.RetrofitClientImpl
import com.example.playlistmaker.data.settings.SettingsRepoImpl
import com.example.playlistmaker.data.settings.dto.ThemeCode
import com.example.playlistmaker.data.storage.repo_impl.SharedPrefsList
import com.example.playlistmaker.data.storage.repo_impl.SharedPrefsTrack
import com.example.playlistmaker.domain.entities.Track
import com.example.playlistmaker.domain.search.SearchRepository
import com.example.playlistmaker.domain.search.use_cases_impl.SearchUseCaseImpl
import com.example.playlistmaker.domain.settings.SettingsRepository
import com.example.playlistmaker.domain.settings.use_cases_impl.GetThemeCodeUseCase
import com.example.playlistmaker.domain.settings.use_cases_impl.StoreThemeCodeUseCase
import com.example.playlistmaker.domain.settings.use_cases_impl.SwitchThemeUseCase
import com.example.playlistmaker.domain.storage.StorageManagerRepo
import com.example.playlistmaker.domain.storage.use_cases_impl.GetTrackListUseCase
import com.example.playlistmaker.domain.storage.use_cases_impl.GetTrackUseCase
import com.example.playlistmaker.domain.storage.use_cases_impl.StoreTrackListUseCase
import com.example.playlistmaker.domain.storage.use_cases_impl.StoreTrackUseCase
import com.example.playlistmaker.domain.storage.use_cases.GetDataUseCase
import com.example.playlistmaker.domain.storage.use_cases.StoreDataUseCase

object Creator {
    private var storageManagerTrack: StorageManagerRepo<Track?>? = null
    private var storageManagerList: StorageManagerRepo<List<Track>>? = null
    private var searchRepository: SearchRepository? = null
    private var settingsRepository: SettingsRepository? = null

    fun createStoreTrackUseCase(context: Context): StoreDataUseCase<Track> {
        if (storageManagerTrack == null) storageManagerTrack = SharedPrefsTrack(context)
        return StoreTrackUseCase(storageManagerTrack!!)
    }

    fun createGetTrackUseCase(context: Context): GetDataUseCase<Track?> {
        if (storageManagerTrack == null) storageManagerTrack = SharedPrefsTrack(context)
        return GetTrackUseCase(storageManagerTrack!!)
    }

    fun createStoreTrackListUseCase(context: Context): StoreDataUseCase<List<Track>> {
        if (storageManagerList == null) storageManagerList = SharedPrefsList(context)
        return StoreTrackListUseCase(storageManagerList!!)
    }

    fun createGetTrackListUseCase(context: Context): GetDataUseCase<List<Track>> {
        if (storageManagerList == null) storageManagerList = SharedPrefsList(context)
        return GetTrackListUseCase(storageManagerList!!)
    }

    fun createSearchUseCase(): GetDataUseCase<List<Track>?> {
        if (searchRepository == null) searchRepository = SearchRepoImpl(RetrofitClientImpl())

        return SearchUseCaseImpl(searchRepository!!)
    }

    fun createGetThemeCodeUseCase(context: Context): GetDataUseCase<ThemeCode?> {
        if (settingsRepository == null) settingsRepository = SettingsRepoImpl(context)
        return GetThemeCodeUseCase(settingsRepository!!)
    }

    fun createStoreThemeCodeUseCase(context: Context): StoreDataUseCase<ThemeCode> {
        if (settingsRepository == null) settingsRepository = SettingsRepoImpl(context)
        return StoreThemeCodeUseCase(settingsRepository!!)
    }

    fun createSwitchThemeUseCase(context: Context): SwitchThemeUseCase {
        if (settingsRepository == null) settingsRepository = SettingsRepoImpl(context)
        return SwitchThemeUseCase(settingsRepository!!)

    }

}