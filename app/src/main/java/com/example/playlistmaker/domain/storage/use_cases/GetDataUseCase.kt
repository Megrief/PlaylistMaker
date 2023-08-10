package com.example.playlistmaker.domain.storage.use_cases

import java.util.function.Consumer

interface GetDataUseCase<T> {
    fun get(key: String, consumer: Consumer<T>)
}