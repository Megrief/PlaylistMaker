package com.example.playlistmaker.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.playlistmaker.databinding.FragmentSettingsBinding
import com.example.playlistmaker.ui.main.RootActivity
import com.example.playlistmaker.ui.settings.view_model.SettingsViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel


class SettingsFragment : Fragment() {
    private var _binding: FragmentSettingsBinding? = null
    private val binding: FragmentSettingsBinding
        get() = _binding!!

    private val viewModel: SettingsViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        with((requireActivity() as RootActivity).binding.bottomNav) {
            if (isGone) visibility = View.VISIBLE
        }
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getScreenState().observe(viewLifecycleOwner) { screenState ->
            binding.darkTheme.isChecked = screenState.theme.flag
        }

        with(binding) {
            lifecycleScope.launch(Dispatchers.IO) {
                darkTheme.setOnCheckedChangeListener { _, checked ->
                    viewModel.switchTheme(checked)
                }
                share.setOnClickListener {
                    viewModel.shareApp()
                }
                userAgreement.setOnClickListener {
                    viewModel.openUserAgreement()
                }
                support.setOnClickListener {
                    viewModel.mailToSupport()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}