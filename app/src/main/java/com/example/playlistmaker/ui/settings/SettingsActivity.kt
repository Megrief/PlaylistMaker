package com.example.playlistmaker.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import androidx.activity.ComponentActivity
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmaker.databinding.ActivitySettingsBinding
import com.example.playlistmaker.ui.settings.view_model.SettingsViewModel

class SettingsActivity : ComponentActivity() {
    private val binding by lazy { ActivitySettingsBinding.inflate(LayoutInflater.from(this)) }
    private lateinit var viewModel: SettingsViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        // new
        viewModel = ViewModelProvider(this, SettingsViewModel.getSettingsViewModelFactory())[SettingsViewModel::class.java]
        viewModel.getScreenState().observe(this) { screenState ->
            binding.darkTheme.isChecked = screenState.theme.code
            binding.share.setOnClickListener { startActivity(screenState.shareApp) }
            binding.support.setOnClickListener { startActivity(screenState.mailToSupport) }
            binding.userAgreement.setOnClickListener { startActivity(screenState.userAgreement) }
        }

        ////

        binding.toolbar.setNavigationOnClickListener { onBackPressedDispatcher.onBackPressed() }
        configureThemeSwitcher()
    }


// Theme changes but just on main screen and not implemented screen.
// Maybe problem is hiding in application class.
    private fun configureThemeSwitcher() {
        binding.darkTheme.setOnCheckedChangeListener { _, checked ->
            viewModel.switchTheme(checked)
        }
    }
}