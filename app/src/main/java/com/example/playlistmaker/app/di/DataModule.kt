package com.example.playlistmaker.app.di

import android.content.Context
import android.content.SharedPreferences
import android.content.res.Resources
import com.example.playlistmaker.app.App
import com.example.playlistmaker.data.media.repo_impl.SharedPrefsPlaylist
import com.example.playlistmaker.data.media.repo_impl.SharedPrefsPlaylistList
import com.example.playlistmaker.data.search.SearchRepoImpl
import com.example.playlistmaker.data.search.network.api.ITunesApiService
import com.example.playlistmaker.data.search.network.network_client.NetworkClient
import com.example.playlistmaker.data.search.network.network_client.impl.RetrofitClientImpl
import com.example.playlistmaker.data.settings.SettingsRepoImpl
import com.example.playlistmaker.data.sharing.external_navigator.ExternalNavigator
import com.example.playlistmaker.data.sharing.external_navigator.impl.ExternalNavigatorImpl
import com.example.playlistmaker.data.sharing.repo_impl.SharingRepoImpl
import com.example.playlistmaker.data.storage.repo_impl.SharedPrefsList
import com.example.playlistmaker.data.storage.repo_impl.SharedPrefsTrack
import com.example.playlistmaker.domain.entity.Track
import com.example.playlistmaker.domain.media.entity.Playlist
import com.example.playlistmaker.domain.search.SearchRepository
import com.example.playlistmaker.domain.settings.SettingsRepository
import com.example.playlistmaker.domain.sharing.SharingRepository
import com.example.playlistmaker.domain.storage.StorageManagerRepo
import com.google.gson.Gson
import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

const val STORAGE_MANAGER_REPO_LIST = "StorageManagerRepoList"
const val STORAGE_MANAGER_REPO_TRACK = "StorageManagerRepoTrack"
const val  STORAGE_MANAGER_REPO_PLAYLIST = "StorageManagerRepoPlaylist"
const val STORAGE_MANAGER_REPO_PLAYLIST_LIST = "StorageManagerRepoPlaylistList"

val dataModule = module {
    single<NetworkClient> {
        RetrofitClientImpl(apiService = get())
    }

    single<SearchRepository> {
        SearchRepoImpl(networkClient = get())
    }

    factory {
        Gson()
    }

    factory<Resources> {
        val context: Context = get()
        context.resources
    }

    factory<ITunesApiService> {
        val baseUrl = "https://itunes.apple.com"
        val retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        retrofit.create(ITunesApiService::class.java)
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
        SharingRepoImpl(resources = get(), externalNavigator = get())
    }

    single<StorageManagerRepo<List<Track>>>(named(STORAGE_MANAGER_REPO_LIST)) {
        SharedPrefsList(sharedPrefs = get(), gson = get())
    }

    single<StorageManagerRepo<Track?>>(named(STORAGE_MANAGER_REPO_TRACK)) {
        SharedPrefsTrack(sharedPrefs = get(), gson = get())
    }

    single<StorageManagerRepo<Playlist?>>(named(STORAGE_MANAGER_REPO_PLAYLIST)) {
        SharedPrefsPlaylist(sharedPrefs = get(), gson = get())
    }

    single<StorageManagerRepo<List<Playlist>>>(named(STORAGE_MANAGER_REPO_PLAYLIST_LIST)) {
        SharedPrefsPlaylistList(sharedPrefs = get(), gson = get())
    }
}