package com.example.playlistmaker.domain.storage.repos

import com.example.playlistmaker.domain.storage.repos.basic_repos.GetDataRepo
import com.example.playlistmaker.domain.storage.repos.basic_repos.StoreDataRepo

interface StorageManagerRepo<T> : GetDataRepo<T>, StoreDataRepo<T>