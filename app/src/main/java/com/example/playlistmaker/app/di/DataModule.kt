package com.example.playlistmaker.app.di

import android.content.Context
import android.content.SharedPreferences
import android.content.res.Resources
import android.net.Uri
import androidx.room.Room
import com.example.playlistmaker.data.storage.db.AppDb
import com.example.playlistmaker.data.storage.db.repo_impl.TrackDbRepoImpl
import com.example.playlistmaker.data.search.SearchRepoImpl
import com.example.playlistmaker.data.search.network.api.ITunesApiService
import com.example.playlistmaker.data.search.network.network_client.NetworkClient
import com.example.playlistmaker.data.search.network.network_client.impl.RetrofitClientImpl
import com.example.playlistmaker.data.settings.SettingsRepoImpl
import com.example.playlistmaker.data.sharing.external_navigator.ExternalNavigator
import com.example.playlistmaker.data.sharing.external_navigator.impl.ExternalNavigatorImpl
import com.example.playlistmaker.data.sharing.repo_impl.SharingRepoImpl
import com.example.playlistmaker.data.storage.db.repo_impl.PlaylistDbRepoImpl
import com.example.playlistmaker.data.storage.external_storage.ExternalStorageRepoImpl
import com.example.playlistmaker.data.storage.shared_prefs.repo_impl.SharedPrefsList
import com.example.playlistmaker.data.storage.shared_prefs.repo_impl.SharedPrefsTrack
import com.example.playlistmaker.domain.storage.db.DbRepo
import com.example.playlistmaker.domain.entity.Track
import com.example.playlistmaker.domain.media.entity.Playlist
import com.example.playlistmaker.domain.search.SearchRepository
import com.example.playlistmaker.domain.settings.SettingsRepository
import com.example.playlistmaker.domain.sharing.SharingRepository
import com.example.playlistmaker.domain.storage.StorageManagerRepo
import com.example.playlistmaker.domain.storage.external_storage.ExternalStorageRepo
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

const val STORAGE_MANAGER_REPO_LIST = "StorageManagerRepoList"
const val STORAGE_MANAGER_REPO_TRACK = "StorageManagerRepoTrack"
const val DB_REPO_TRACK = "DbRepoTrack"
const val DB_REPO_PLAYLIST = "DbRepoPlaylist"
const val PLAYLIST_MAKER = "PLAYLIST_MAKER"
const val EXTERNAL_STORAGE_REPO_PHOTO = "ExternalStorageRepoPhoto"

val dataModule = module {

    single {
        Room.databaseBuilder(
                androidContext(),
                AppDb::class.java,
                "playlist_maker.db"
            ).build()
    }

    factory {
        val appDb: AppDb = get()
        appDb.trackDbDao
    }

    factory {
        val appDb: AppDb = get()
        appDb.playlistDbDao
    }

    single<DbRepo<Playlist>>(named(DB_REPO_PLAYLIST)) {
        PlaylistDbRepoImpl(playlistDbDao = get())
    }

    single<DbRepo<Track>>(named(DB_REPO_TRACK)) {
        TrackDbRepoImpl(trackDbDao = get())
    }

    single<NetworkClient> {
        RetrofitClientImpl(apiService = get())
    }

    single<SearchRepository> {
        SearchRepoImpl(networkClient = get())
    }

    factory {
        Gson()
    }

    factory {
        CoroutineScope(Dispatchers.IO)
    }

    factory<Resources> {
        androidContext().resources
    }

    factory<ITunesApiService> {
        val baseUrl = "https://itunes.apple.com"
        Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ITunesApiService::class.java)
    }

    single<SettingsRepository> {
        SettingsRepoImpl(
            sharedPrefs = get(),
            context = androidContext(),
            gson = get())
    }

    single<SharedPreferences> {
        androidContext().getSharedPreferences(PLAYLIST_MAKER, Context.MODE_PRIVATE)
    }

    single<ExternalStorageRepo<Uri>>(named(EXTERNAL_STORAGE_REPO_PHOTO)) {
        ExternalStorageRepoImpl(
            context = androidContext()
        )
    }

    single<ExternalNavigator> {
        ExternalNavigatorImpl(context = androidContext())
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
}