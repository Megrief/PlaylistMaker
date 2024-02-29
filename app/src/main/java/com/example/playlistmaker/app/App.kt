package com.example.playlistmaker.app

import android.app.Application
import android.content.IntentFilter
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import com.example.playlistmaker.app.di.GET_THEME_FLAG_USE_CASE
import com.example.playlistmaker.app.di.STORE_THEME_FLAG_USE_CASE
import com.example.playlistmaker.app.di.dataModule
import com.example.playlistmaker.app.di.domainModule
import com.example.playlistmaker.app.di.presentationModule
import com.example.playlistmaker.domain.settings.entity.ThemeFlag
import com.example.playlistmaker.domain.storage.use_cases.GetItemUseCase
import com.example.playlistmaker.domain.storage.use_cases.StoreItemUseCase
import com.example.playlistmaker.utils.SwitchThemeReceiver
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.core.qualifier.named

class App : Application() {
    private val getThemeFlagUseCase: GetItemUseCase<ThemeFlag?> by inject(named(GET_THEME_FLAG_USE_CASE))
    private val storeThemeFlagUseCase: StoreItemUseCase<ThemeFlag> by inject(named(STORE_THEME_FLAG_USE_CASE))
    private val coroutineScope: CoroutineScope by inject()
    private val switchThemeReceiver: SwitchThemeReceiver by inject()

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@App)

            modules(dataModule, domainModule, presentationModule)
        }
        getCurrentTheme()

        registerReceiver()
    }

    private fun getCurrentTheme() {
        coroutineScope.launch {
            getThemeFlagUseCase.get().collect { flag ->
                withContext(Dispatchers.Main) {
                    AppCompatDelegate.setDefaultNightMode(
                        if (flag != null) {
                            if (flag.flag) AppCompatDelegate.MODE_NIGHT_YES
                            else AppCompatDelegate.MODE_NIGHT_NO
                        } else AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
                    )
                }
            }
        }
    }

    private fun switchTheme(darkThemeEnabled: Boolean) {
        coroutineScope.launch {
            storeThemeFlagUseCase.store(ThemeFlag(darkThemeEnabled))
        }
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) AppCompatDelegate.MODE_NIGHT_YES
            else AppCompatDelegate.MODE_NIGHT_NO
        )
    }

    private fun registerReceiver() {
        val intentFilter = IntentFilter().apply { addAction(ACTION_SWITCH_THEME) }
        switchThemeReceiver.onSwitchCallback = ::switchTheme

        ContextCompat.registerReceiver(
            this@App,
            switchThemeReceiver,
            intentFilter,
            ContextCompat.RECEIVER_NOT_EXPORTED
        )
    }

    companion object {
        const val ACTION_SWITCH_THEME = ".app.App.actionswitchtheme"
        const val THEME_FLAG = "ThemeFlag"
    }
}