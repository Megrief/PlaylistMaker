package com.example.playlistmaker.data.search.network

open class Response(var resultCode: Int = 0) {

    companion object {
        const val SUCCESS = 200
        const val FAILURE = 400
    }
}