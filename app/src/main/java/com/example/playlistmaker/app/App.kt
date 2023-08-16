package com.example.playlistmaker.app

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.example.playlistmaker.app.di.dataModule
import com.example.playlistmaker.app.di.domainModule
import com.example.playlistmaker.app.di.presentationModule
import com.example.playlistmaker.domain.settings.entity.ThemeFlag
import com.example.playlistmaker.domain.storage.use_cases.GetDataUseCase
import com.example.playlistmaker.domain.storage.use_cases.StoreDataUseCase
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.core.qualifier.named

class App : Application() {
//    private val getThemeFlagUseCase by lazy { Creator.createGetThemeFlagUseCase(this) }
//    private val storeThemeFlagUseCase by lazy { Creator.createStoreThemeFlagUseCase(this) }
    private val getThemeFlagUseCase: GetDataUseCase<ThemeFlag?> by inject(named("GetThemeFlagUseCase"))
    private val storeThemeFlagUseCase: StoreDataUseCase<ThemeFlag> by inject(named("StoreThemeFlagUseCase"))
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@App)

            modules(dataModule, domainModule, presentationModule)
        }
//        val getThemeFlagUseCase: GetThemeFlagUseCase = getKoin().get()
        getThemeFlagUseCase.get(THEME) {
            AppCompatDelegate.setDefaultNightMode(
                if (it != null) {
                    if (it.flag) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
                } else AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
            )
        }
    }

    fun switchTheme(darkThemeEnabled: Boolean) {
//        val storeThemeFlagUseCase: StoreThemeFlagUseCase = getKoin().get()
        storeThemeFlagUseCase.store(THEME, ThemeFlag(darkThemeEnabled))
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }

    companion object {
        const val PLAYLIST_MAKER = "PLAYLIST_MAKER"
        const val THEME = "THEME"
    }
}