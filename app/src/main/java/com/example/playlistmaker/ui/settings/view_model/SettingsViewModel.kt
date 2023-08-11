package com.example.playlistmaker.ui.settings.view_model

import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.playlistmaker.app.App
import com.example.playlistmaker.domain.settings.entity.ThemeFlag
import com.example.playlistmaker.domain.settings.use_cases_impl.SwitchThemeUseCase
import com.example.playlistmaker.domain.sharing.SharingRepository
import com.example.playlistmaker.domain.storage.use_cases.GetDataUseCase
import com.example.playlistmaker.ui.settings.utils.SingleLiveEvent
import com.example.playlistmaker.utils.creator.Creator

class SettingsViewModel(
    application: App,
    getThemeCodeUseCase: GetDataUseCase<ThemeFlag?>,
    private val switchThemeUseCase: SwitchThemeUseCase,
    private val sharingRepository: SharingRepository
) : AndroidViewModel(application) {

    private val darkThemeState = SingleLiveEvent<DarkThemeState>()

    init {
        darkThemeState.apply {
            getThemeCodeUseCase.get(App.THEME) {
                postValue(
                    DarkThemeState(
                        theme = it ?: ThemeFlag(false)
                    )
                )
            }
        }
    }

    fun getScreenState(): LiveData<DarkThemeState> = darkThemeState

    fun switchTheme(checked: Boolean) {
        switchThemeUseCase.switchTheme(checked)
    }

    fun shareApp() {
        sharingRepository.shareApp()
    }
    fun openUserAgreement() {
        sharingRepository.openUserAgreement()
    }

    fun mailToSupport() {
        sharingRepository.mailToSupport()
    }
    companion object {
        fun getSettingsViewModelFactory(): ViewModelProvider.Factory {
            return viewModelFactory {
                initializer {
                    val application = this[APPLICATION_KEY] as App
                    val getThemeCodeUseCase = Creator.createGetThemeFlagUseCase(application.applicationContext)
                    val switchThemeUseCase = Creator.createSwitchThemeUseCase(application.applicationContext)
                    val sharingRepository = Creator.createSharingRepository(application.applicationContext)
                    SettingsViewModel(
                        application,
                        getThemeCodeUseCase,
                        switchThemeUseCase,
                        sharingRepository
                    )
                }
            }
        }
    }
}