package com.example.playlistmaker.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmaker.databinding.ActivitySettingsBinding
import com.example.playlistmaker.ui.settings.view_model.SettingsViewModel

class SettingsActivity : AppCompatActivity() {
    private val binding by lazy { ActivitySettingsBinding.inflate(LayoutInflater.from(this)) }
    private lateinit var viewModel: SettingsViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this, SettingsViewModel.getSettingsViewModelFactory())[SettingsViewModel::class.java]
        viewModel.getScreenState().observe(this) { screenState ->
            binding.darkTheme.isChecked = screenState.theme.flag
        }
        with(binding) {
            toolbar.setNavigationOnClickListener { onBackPressedDispatcher.onBackPressed() }
            darkTheme.setOnCheckedChangeListener { _, checked ->
                viewModel.switchTheme(checked)
            }
            share.setOnClickListener { viewModel.shareApp() }
            userAgreement.setOnClickListener { viewModel.openUserAgreement() }
            support.setOnClickListener { viewModel.mailToSupport() }
        }
    }
}