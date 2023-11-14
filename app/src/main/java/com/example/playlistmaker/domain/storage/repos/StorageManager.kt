package com.example.playlistmaker.domain.storage.repos

import com.example.playlistmaker.domain.storage.repos.basic_repos.GetDataRepo
import com.example.playlistmaker.domain.storage.repos.basic_repos.StoreDataRepo

interface StorageManager<T> : GetDataRepo<T>, StoreDataRepo<T>