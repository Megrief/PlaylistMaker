package com.example.playlistmaker.domain.storage.repos.basic_repos

interface DeleteDataRepo<T> {
    fun delete(item: T)
}