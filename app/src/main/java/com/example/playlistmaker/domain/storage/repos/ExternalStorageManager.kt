package com.example.playlistmaker.domain.storage.repos

import com.example.playlistmaker.domain.storage.repos.basic_repos.GetDataByIdRepo
import com.example.playlistmaker.domain.storage.repos.basic_repos.StoreDataRepo

interface ExternalStorageManager<T> : GetDataByIdRepo<T>, StoreDataRepo<T>