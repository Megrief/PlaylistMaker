package com.example.playlistmaker.creator

import android.content.Context
import com.example.playlistmaker.data.network.RetrofitClientImpl
import com.example.playlistmaker.data.repo_impl.SearchRepoImpl
import com.example.playlistmaker.data.repo_impl.SharedPrefsList
import com.example.playlistmaker.data.repo_impl.SharedPrefsTrack
import com.example.playlistmaker.domain.entities.Track
import com.example.playlistmaker.domain.interactors.SearchInteractor
import com.example.playlistmaker.domain.interactors.StorageInteractor
import com.example.playlistmaker.domain.interactors_impl.SearchInteractorImpl
import com.example.playlistmaker.domain.interactors_impl.StorageInteractorTrack
import com.example.playlistmaker.domain.interactors_impl.StorageInteractorTrackList
import com.example.playlistmaker.domain.repositories.SearchRepository
import com.example.playlistmaker.domain.repositories.StorageManagerRepo

object Creator {
    private var storageManagerTrack: StorageManagerRepo<Track?>? = null
    private var storageManagerList: StorageManagerRepo<List<Track>>? = null
    private var searchRepository: SearchRepository? = null

    fun createStorageInteractorTrack(context: Context): StorageInteractor<Track?> {
        if (storageManagerTrack == null) storageManagerTrack = SharedPrefsTrack(context)
        return StorageInteractorTrack(storageManagerTrack!!)
    }

    fun createStorageInteractorList(context: Context): StorageInteractor<List<Track>> {
        if (storageManagerList == null) storageManagerList = SharedPrefsList(context)
        return StorageInteractorTrackList(storageManagerList!!)
    }

    fun createSearchInteractor(): SearchInteractor {
        if (searchRepository == null) searchRepository = SearchRepoImpl(RetrofitClientImpl())

        return SearchInteractorImpl(searchRepository!!)
    }

}