package com.example.playlistmaker.app.di

import android.content.Context
import android.content.SharedPreferences
import com.example.playlistmaker.app.App
import com.example.playlistmaker.data.search.SearchRepoImpl
import com.example.playlistmaker.data.search.network.network_client.NetworkClient
import com.example.playlistmaker.data.search.network.network_client.impl.RetrofitClientImpl
import com.example.playlistmaker.data.settings.SettingsRepoImpl
import com.example.playlistmaker.data.sharing.external_navigator.ExternalNavigator
import com.example.playlistmaker.data.sharing.external_navigator.impl.ExternalNavigatorImpl
import com.example.playlistmaker.data.sharing.repo_impl.SharingRepoImpl
import com.example.playlistmaker.data.storage.repo_impl.SharedPrefsList
import com.example.playlistmaker.data.storage.repo_impl.SharedPrefsTrack
import com.example.playlistmaker.domain.entities.Track
import com.example.playlistmaker.domain.search.SearchRepository
import com.example.playlistmaker.domain.settings.SettingsRepository
import com.example.playlistmaker.domain.sharing.SharingRepository
import com.example.playlistmaker.domain.storage.StorageManagerRepo
import com.google.gson.Gson
import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.named
import org.koin.dsl.module

val dataModule = module {
    single<NetworkClient> {
        RetrofitClientImpl()
    }

    single<SearchRepository> {
        SearchRepoImpl(networkClient = get())
    }

    factory {
        Gson()
    }

    single<SettingsRepository> {
        SettingsRepoImpl(context = get(), gson = get())
    }

    single<SharedPreferences> {
        androidContext().getSharedPreferences(App.PLAYLIST_MAKER, Context.MODE_PRIVATE)
    }

    single<ExternalNavigator> {
        ExternalNavigatorImpl(context = get())
    }

    single<SharingRepository> {
        SharingRepoImpl(context = get(), externalNavigator = get())
    }

    single<StorageManagerRepo<List<Track>>>(named("StorageManagerRepoList")) {
        SharedPrefsList(sharedPrefs = get(), gson = get())
    }

    single<StorageManagerRepo<Track?>>(named("StorageManagerRepoTrack")) {
        SharedPrefsTrack(sharedPrefs = get(), gson = get())
    }
}