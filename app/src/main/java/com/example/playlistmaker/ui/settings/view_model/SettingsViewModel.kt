package com.example.playlistmaker.ui.settings.view_model

import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.playlistmaker.R
import com.example.playlistmaker.app.App
import com.example.playlistmaker.data.settings.dto.ThemeCode
import com.example.playlistmaker.domain.settings.use_cases_impl.SwitchThemeUseCase
import com.example.playlistmaker.domain.use_cases.GetDataUseCase
import com.example.playlistmaker.ui.settings.SettingsScreenState
import com.example.playlistmaker.utils.creator.Creator

class SettingsViewModel(
    application: App,
    private val getThemeCodeUseCase: GetDataUseCase<ThemeCode?>,
    private val switchThemeUseCase: SwitchThemeUseCase
) : AndroidViewModel(application) {

    private val screenState = MutableLiveData<SettingsScreenState>()

    init {
        screenState.apply {
            val shareAppIntent: Intent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, application.getString(R.string.android_developer))
            }
            val url = Uri.parse(application.getString(R.string.practicum_offer))
            val userAgreementIntent = Intent(Intent.ACTION_VIEW, url)
            val supportIntent: Intent = Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("mailto:")
                with(application) {
                    putExtra(Intent.EXTRA_EMAIL, getString(R.string.supp_message_address))
                    putExtra(Intent.EXTRA_SUBJECT, getString(R.string.supp_message_theme))
                    putExtra(Intent.EXTRA_TEXT, getString(R.string.supp_message_content))
                }
            }
            getThemeCodeUseCase.get(App.THEME) {
                postValue(
                    SettingsScreenState(
                        theme = it ?: ThemeCode(ThemeCode.SYSTEM_MODE_CODE),
                        mailToSupport = supportIntent,
                        userAgreement = userAgreementIntent,
                        shareApp = shareAppIntent
                    )
                )
            }


        }
    }

    fun getScreenState(): LiveData<SettingsScreenState> = screenState

    fun switchTheme(checked: Boolean) {
        Log.wtf("SWITCH", "In viewModel switched")
        switchThemeUseCase.switchTheme(checked)
        getThemeCodeUseCase.get(App.THEME) {
            if (it != null) screenState.value = getScreenState().value?.copy(theme = it)
        }
    }

    companion object {

        fun getSettingsViewModelFactory(): ViewModelProvider.Factory {
            return viewModelFactory {
                initializer {
                    val application = this[APPLICATION_KEY] as App
                    val getThemeCodeUseCase = Creator.createGetThemeCodeUseCase(application)
                    val switchThemeUseCase = Creator.createSwitchThemeUseCase(application)
                    SettingsViewModel(
                        application,
                        getThemeCodeUseCase,
                        switchThemeUseCase
                    )
                }
            }
        }
    }
}