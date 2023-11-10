package com.example.playlistmaker.ui.settings.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.settings.entity.ThemeFlag
import com.example.playlistmaker.domain.settings.use_cases_impl.SwitchThemeUseCase
import com.example.playlistmaker.domain.sharing.SharingRepository
import com.example.playlistmaker.domain.storage.use_cases.GetItemUseCase
import com.example.playlistmaker.utils.SingleLiveEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SettingsViewModel(
    getThemeFlagUseCase: GetItemUseCase<ThemeFlag?>,
    private val switchThemeUseCase: SwitchThemeUseCase,
    private val sharingRepository: SharingRepository,
    private val darkThemeState: SingleLiveEvent<DarkThemeState>
) : ViewModel() {

    init {
        viewModelScope.launch(Dispatchers.IO) {
            darkThemeState.apply {
                getThemeFlagUseCase.get().collect {
                    postValue(DarkThemeState(theme = it ?: ThemeFlag(false)))
                }
            }
        }
    }

    fun getScreenState(): LiveData<DarkThemeState> = darkThemeState

    fun switchTheme(checked: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            switchThemeUseCase.switchTheme(checked)
        }
    }

    fun shareApp() {
        sharingRepository.share(null)
    }

    fun openUserAgreement() {
        sharingRepository.openUserAgreement()
    }

    fun mailToSupport() {
        sharingRepository.mailToSupport()
    }
}