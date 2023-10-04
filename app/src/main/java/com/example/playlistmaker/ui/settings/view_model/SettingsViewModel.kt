package com.example.playlistmaker.ui.settings.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.app.App
import com.example.playlistmaker.domain.settings.entity.ThemeFlag
import com.example.playlistmaker.domain.settings.use_cases_impl.SwitchThemeUseCase
import com.example.playlistmaker.domain.sharing.SharingRepository
import com.example.playlistmaker.domain.storage.use_cases.GetDataUseCase
import com.example.playlistmaker.ui.settings.utils.SingleLiveEvent
import kotlinx.coroutines.launch
import org.koin.java.KoinJavaComponent.getKoin

class SettingsViewModel(
    getThemeFlagUseCase: GetDataUseCase<ThemeFlag?>,
    private val switchThemeUseCase: SwitchThemeUseCase,
    private val sharingRepository: SharingRepository
) : ViewModel() {
    private val darkThemeState: SingleLiveEvent<DarkThemeState> = getKoin().get()

    init {
        viewModelScope.launch {
            darkThemeState.apply {
                getThemeFlagUseCase.get(App.THEME).collect {
                    postValue(DarkThemeState(theme = it ?: ThemeFlag(false)))
                }
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
}