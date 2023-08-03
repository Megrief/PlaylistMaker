package com.example.playlistmaker.ui.audioplayer.vm.player

sealed class PlayerStatus {
    data class Playing(var currentPosition: String) : PlayerStatus()
    object Prepared : PlayerStatus()
    object Default : PlayerStatus()
    object Paused : PlayerStatus()
}