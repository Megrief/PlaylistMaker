package com.example.playlistmaker.domain.storage.repos

import com.example.playlistmaker.domain.storage.repos.basic_repos.DeleteDataRepo
import com.example.playlistmaker.domain.storage.repos.basic_repos.GetByIdRepo
import com.example.playlistmaker.domain.storage.repos.basic_repos.GetDataRepo
import com.example.playlistmaker.domain.storage.repos.basic_repos.StoreDataRepo

interface DbManagerRepo<T> : StoreDataRepo<T>, GetDataRepo<T>, GetByIdRepo<T>, DeleteDataRepo<T>