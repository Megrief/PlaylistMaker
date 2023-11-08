package com.example.playlistmaker.domain.storage.repos

import com.example.playlistmaker.domain.storage.repos.basic_repos.GetByIdRepo
import com.example.playlistmaker.domain.storage.repos.basic_repos.StoreDataRepo

interface ExternalStorageManagerRepo<T> : GetByIdRepo<T>, StoreDataRepo<T>