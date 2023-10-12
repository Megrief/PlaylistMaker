package com.example.playlistmaker.app

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.example.playlistmaker.app.di.GET_THEME_FLAG_USE_CASE
import com.example.playlistmaker.app.di.STORE_THEME_FLAG_USE_CASE
import com.example.playlistmaker.app.di.dataModule
import com.example.playlistmaker.app.di.domainModule
import com.example.playlistmaker.app.di.presentationModule
import com.example.playlistmaker.domain.settings.entity.ThemeFlag
import com.example.playlistmaker.domain.storage.use_cases.GetDataUseCase
import com.example.playlistmaker.domain.storage.use_cases.StoreDataUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.core.qualifier.named

class App : Application() {
    private val getThemeFlagUseCase: GetDataUseCase<ThemeFlag?> by inject(named(GET_THEME_FLAG_USE_CASE))
    private val storeThemeFlagUseCase: StoreDataUseCase<ThemeFlag> by inject(named(STORE_THEME_FLAG_USE_CASE))
    private val coroutineScope: CoroutineScope by inject()
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@App)

            modules(dataModule, domainModule, presentationModule)
        }

        coroutineScope.launch {
            getThemeFlagUseCase.get().collect { flag ->
                AppCompatDelegate.setDefaultNightMode(
                    if (flag != null) {
                        if (flag.flag) AppCompatDelegate.MODE_NIGHT_YES
                        else AppCompatDelegate.MODE_NIGHT_NO
                    } else AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
                )
            }
        }
    }

    fun switchTheme(darkThemeEnabled: Boolean) {
        coroutineScope.launch {
            storeThemeFlagUseCase.store(ThemeFlag(darkThemeEnabled))
        }
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) AppCompatDelegate.MODE_NIGHT_YES
            else AppCompatDelegate.MODE_NIGHT_NO
        )
    }

    companion object {
        const val PLAYLIST_MAKER = "PLAYLIST_MAKER"
    }
}