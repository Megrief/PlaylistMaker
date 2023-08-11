package com.example.playlistmaker.ui.settings.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
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
    getThemeCodeUseCase: GetDataUseCase<ThemeFlag?>,
    private val switchThemeUseCase: SwitchThemeUseCase,
    private val sharingRepository: SharingRepository
) : ViewModel() {

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
                    val context = (this[APPLICATION_KEY] as App).applicationContext
                    val getThemeCodeUseCase = Creator.createGetThemeFlagUseCase(context)
                    val switchThemeUseCase = Creator.createSwitchThemeUseCase(context)
                    val sharingRepository = Creator.createSharingRepository(context)
                    SettingsViewModel(
                        getThemeCodeUseCase,
                        switchThemeUseCase,
                        sharingRepository
                    )
                }
            }
        }
    }
}