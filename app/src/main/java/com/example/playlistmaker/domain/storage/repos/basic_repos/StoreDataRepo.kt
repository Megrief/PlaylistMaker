package com.example.playlistmaker.domain.storage.repos.basic_repos

interface StoreDataRepo<T> {
    fun store(item: T): Boolean
}