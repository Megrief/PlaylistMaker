package com.example.playlistmaker.app.di

import android.net.Uri
import com.example.playlistmaker.domain.entities.Playlist
import com.example.playlistmaker.domain.entities.Track
import com.example.playlistmaker.domain.media.favorites.use_cases_impl.DeleteTrackUseCaseImpl
import com.example.playlistmaker.domain.media.favorites.use_cases_impl.GetFavoritesUseCaseImpl
import com.example.playlistmaker.domain.media.favorites.use_cases_impl.GetTrackByIdUseCaseImpl
import com.example.playlistmaker.domain.media.favorites.use_cases_impl.StoreTrackInDbUseCaseImpl
import com.example.playlistmaker.domain.media.playlists.use_cases_impl.DeletePlaylistUseCase
import com.example.playlistmaker.domain.media.playlists.use_cases_impl.GetPhotoByIdUseCaseImpl
import com.example.playlistmaker.domain.media.playlists.use_cases_impl.GetPlaylistByIdUseCase
import com.example.playlistmaker.domain.media.playlists.use_cases_impl.GetPlaylistsUseCaseImpl
import com.example.playlistmaker.domain.media.playlists.use_cases_impl.StorePhotoUseCaseImpl
import com.example.playlistmaker.domain.media.playlists.use_cases_impl.StorePlaylistInDbUseCaseImpl
import com.example.playlistmaker.domain.playlist_page.SharePlaylistUseCase
import com.example.playlistmaker.domain.search.SearchUseCase
import com.example.playlistmaker.domain.search.use_cases_impl.SearchUseCaseImpl
import com.example.playlistmaker.domain.settings.SettingsRepository
import com.example.playlistmaker.domain.settings.entity.ThemeFlag
import com.example.playlistmaker.domain.settings.use_cases_impl.GetThemeFlagUseCase
import com.example.playlistmaker.domain.settings.use_cases_impl.StoreThemeFlagUseCase
import com.example.playlistmaker.domain.settings.use_cases_impl.SwitchThemeUseCase
import com.example.playlistmaker.domain.sharing.use_cases.ShareStringUseCase
import com.example.playlistmaker.domain.storage.repos.DbManagerRepoData
import com.example.playlistmaker.domain.storage.repos.ExternalStorageManagerRepoData
import com.example.playlistmaker.domain.storage.repos.StorageManagerRepo
import com.example.playlistmaker.domain.storage.shared_prefs_use_cases.GetPhotoIdUseCase
import com.example.playlistmaker.domain.storage.shared_prefs_use_cases.GetTrackListUseCase
import com.example.playlistmaker.domain.storage.shared_prefs_use_cases.GetTrackUseCase
import com.example.playlistmaker.domain.storage.shared_prefs_use_cases.StorePhotoIdUseCase
import com.example.playlistmaker.domain.storage.shared_prefs_use_cases.StoreTrackListUseCase
import com.example.playlistmaker.domain.storage.shared_prefs_use_cases.StoreTrackUseCase
import com.example.playlistmaker.domain.storage.use_cases.DeleteItemUseCase
import com.example.playlistmaker.domain.storage.use_cases.GetItemByIdUseCase
import com.example.playlistmaker.domain.storage.use_cases.GetItemUseCase
import com.example.playlistmaker.domain.storage.use_cases.StoreItemUseCase
import org.koin.core.qualifier.named
import org.koin.dsl.module

const val SEARCH_USE_CASE = "SearchUseCase"
const val GET_THEME_FLAG_USE_CASE = "GetThemeFlagUseCase"
const val STORE_THEME_FLAG_USE_CASE = "StoreThemeFlagUseCase"
const val GET_TRACK_USE_CASE = "GetTrackUseCase"
const val GET_TRACK_LIST_USE_CASE = "GetTrackListUseCase"
const val STORE_TRACK_LIST_USE_CASE = "StoreTrackListUseCase"
const val STORE_TRACK_USE_CASE = "StoreTrackUseCase"
const val STORE_PHOTO_USE_CASE = "StorePhotoUseCase"
const val DELETE_TRACK_USE_CASE = "DeleteTrackUseCase"
const val GET_TRACK_BY_ID_USE_CASE = "GetTrackByIdUseCase"
const val GET_FAVOURITES_USE_CASE = "GetFavouritesUseCase"
const val STORE_TRACK_IN_DB_USE_CASE = "StoreTrackInDbUseCase"
const val STORE_PLAYLIST_IN_DB_USE_CASE = "StorePlaylistInDbUseCase"
const val GET_PLAYLISTS_USE_CASE = "GetPlaylistsUseCase"
const val DELETE_PLAYLIST_USE_CASE = "DeletePlaylistUseCase"
const val GET_TRACK_IN_PLAYLIST_BY_ID = "GetTRackInPlaylistById"
const val DELETE_TRACK_IN_PLAYLIST = "DeleteTrackInPlaylist"
const val STORE_TRACK_IN_PLAYLIST_DB = "StoreTrackInPlaylistDb"
const val GET_PHOTO_BY_ID_USE_CASE = "GetPhotoByIdUseCase"
const val GET_ID_USE_CASE = "GetIdUseCase"
const val STORE_ID_USE_CASE = "StoreIdUseCase"
const val GET_PLAYLIST_BY_ID_USE_CASE = "GetPlaylistByIdUseCase"
const val SHARE_PLAYLIST_USE_CASE = "SharePlaylistUseCase"

val domainModule = module {

    factory<ShareStringUseCase>(named(SHARE_PLAYLIST_USE_CASE)) {
        SharePlaylistUseCase(
            repository = get()
        )
    }

    factory<GetItemByIdUseCase<Uri>>(named(GET_PHOTO_BY_ID_USE_CASE)) {
        val repository: ExternalStorageManagerRepoData<Uri> = get(named(EXTERNAL_STORAGE_REPO_PHOTO))
        GetPhotoByIdUseCaseImpl(
            repository = repository
        )
    }

    factory<GetItemUseCase<Long>>(named(GET_ID_USE_CASE)) {
        val repository: StorageManagerRepo<Long> = get(named(SHARED_PREFS_ID))
        GetPhotoIdUseCase(
            repository = repository
        )
    }

    factory<StoreItemUseCase<Long>>(named(STORE_ID_USE_CASE)) {
        val repository: StorageManagerRepo<Long> = get(named(SHARED_PREFS_ID))
        StorePhotoIdUseCase(
            repository = repository
        )
    }

    factory<StoreItemUseCase<Uri>>(named(STORE_PHOTO_USE_CASE)) {
        val repository:  ExternalStorageManagerRepoData<Uri> = get(named(EXTERNAL_STORAGE_REPO_PHOTO))
        StorePhotoUseCaseImpl(
            repository = repository
        )
    }

    factory<DeleteItemUseCase<Playlist>>(named(DELETE_PLAYLIST_USE_CASE)) {
        val repository: DbManagerRepoData<Playlist> = get(named(DB_REPO_PLAYLIST))
        DeletePlaylistUseCase(
            repository = repository
        )
    }
    factory<DeleteItemUseCase<Track>>(named(DELETE_TRACK_USE_CASE)) {
        val repository: DbManagerRepoData<Track> = get(named(DB_REPO_TRACK))
        DeleteTrackUseCaseImpl(
            repository = repository
        )
    }

    factory<DeleteItemUseCase<Track>>(named(DELETE_TRACK_IN_PLAYLIST)) {
        val repository: DbManagerRepoData<Track> = get(named(DB_REPO_TRACK_IN_PLAYLIST))
        DeleteTrackUseCaseImpl(
            repository = repository
        )
    }

    factory<GetItemByIdUseCase<Playlist>>(named(GET_PLAYLIST_BY_ID_USE_CASE)) {
        val repository: DbManagerRepoData<Playlist> = get(named(DB_REPO_PLAYLIST))
        GetPlaylistByIdUseCase(
            repository = repository
        )
    }
    factory<GetItemByIdUseCase<Track>>(named(GET_TRACK_BY_ID_USE_CASE)) {
        val repository: DbManagerRepoData<Track> = get(named(DB_REPO_TRACK))
        GetTrackByIdUseCaseImpl(
            repository = repository
        )
    }

    factory<GetItemByIdUseCase<Track>>(named(GET_TRACK_IN_PLAYLIST_BY_ID)) {
        val repository: DbManagerRepoData<Track> = get(named(DB_REPO_TRACK_IN_PLAYLIST))
        GetTrackByIdUseCaseImpl(
            repository = repository
        )
    }

    factory<GetItemUseCase<List<Playlist>>>(named(GET_PLAYLISTS_USE_CASE)) {
        val repository: DbManagerRepoData<Playlist> = get(named(DB_REPO_PLAYLIST))
        GetPlaylistsUseCaseImpl(
            repository = repository
        )
    }

    factory<GetItemUseCase<List<Track>>>(named(GET_FAVOURITES_USE_CASE)) {
        val repository: DbManagerRepoData<Track> = get(named(DB_REPO_TRACK))
        GetFavoritesUseCaseImpl(
            repository = repository
        )
    }

    factory<StoreItemUseCase<Playlist>>(named(STORE_PLAYLIST_IN_DB_USE_CASE)) {
        val repository: DbManagerRepoData<Playlist> = get(named(DB_REPO_PLAYLIST))
        StorePlaylistInDbUseCaseImpl(
            repository = repository
        )
    }

    factory<StoreItemUseCase<Track>>(named(STORE_TRACK_IN_DB_USE_CASE)) {
        val repository: DbManagerRepoData<Track> = get(named(DB_REPO_TRACK))
        StoreTrackInDbUseCaseImpl(
            repository = repository
        )
    }

    factory<StoreItemUseCase<Track>>(named(STORE_TRACK_IN_PLAYLIST_DB)) {
        val repository: DbManagerRepoData<Track> = get(named(DB_REPO_TRACK_IN_PLAYLIST))
        StoreTrackInDbUseCaseImpl(
            repository = repository
        )
    }

    factory<SearchUseCase>(named(SEARCH_USE_CASE)) {
        SearchUseCaseImpl(
            repository = get()
        )
    }

    factory<GetItemUseCase<ThemeFlag?>>(named(GET_THEME_FLAG_USE_CASE)) {
        val repository: SettingsRepository = get()
        GetThemeFlagUseCase(
            repository = repository
        )
    }

    factory<StoreItemUseCase<ThemeFlag>>(named(STORE_THEME_FLAG_USE_CASE)) {
        val repository: SettingsRepository = get()
        StoreThemeFlagUseCase(
            repository = repository
        )
    }

    factory {
        SwitchThemeUseCase(
            repository = get()
        )
    }

    factory<StoreItemUseCase<Track>>(named(STORE_TRACK_USE_CASE)) {
        val repository: StorageManagerRepo<Track> = get(named(SHARED_PREFS_TRACK))
        StoreTrackUseCase(
            repository = repository
        )
    }

    factory<GetItemUseCase<Track>>(named(GET_TRACK_USE_CASE)) {
        val repository: StorageManagerRepo<Track> = get(named(SHARED_PREFS_TRACK))
        GetTrackUseCase(
            repository = repository
        )
    }

    factory<StoreItemUseCase<List<Track>>>(named(STORE_TRACK_LIST_USE_CASE)) {
        val repository: StorageManagerRepo<List<Track>> = get(named(SHARED_PREFS_LIST))
        StoreTrackListUseCase(
            repository = repository
        )
    }

    factory<GetItemUseCase<List<Track>>>(named(GET_TRACK_LIST_USE_CASE)) {
        val repository: StorageManagerRepo<List<Track>> = get(named(SHARED_PREFS_LIST))
        GetTrackListUseCase(
            repository = repository
        )
    }

}