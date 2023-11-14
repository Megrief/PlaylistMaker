package com.example.playlistmaker.utils

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.playlistmaker.app.App

class SwitchThemeReceiver : BroadcastReceiver() {

    var onSwitchCallback: ((Boolean) -> Unit)? = null

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == App.ACTION_SWITCH_THEME) {
            val flag = intent.getBooleanExtra(App.THEME_FLAG, false)
            onSwitchCallback?.invoke(flag)
        }
    }

}