package com.example.playlistmaker.domain.storage.repos

import com.example.playlistmaker.domain.storage.repos.basic_repos.DeleteDataRepo
import com.example.playlistmaker.domain.storage.repos.basic_repos.GetDataByIdRepo
import com.example.playlistmaker.domain.storage.repos.basic_repos.GetDataRepo
import com.example.playlistmaker.domain.storage.repos.basic_repos.StoreDataRepo

interface DbManager<T> : StoreDataRepo<T>, GetDataRepo<T>, GetDataByIdRepo<T>, DeleteDataRepo<T>