package com.example.playlistmaker.domain.interactors

import java.util.function.Consumer

interface StorageInteractor<T> {
    fun save(key: String, item: T)

    fun get(key: String, consumer: Consumer<T>)
}