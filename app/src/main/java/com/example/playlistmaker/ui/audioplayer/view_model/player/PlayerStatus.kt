package com.example.playlistmaker.ui.audioplayer.view_model.player

sealed class PlayerStatus {
    data class Playing(var currentPosition: String) : PlayerStatus()
    class Prepared(val currentPosition: String?) : PlayerStatus()
    object Default : PlayerStatus()
    object Paused : PlayerStatus()
}