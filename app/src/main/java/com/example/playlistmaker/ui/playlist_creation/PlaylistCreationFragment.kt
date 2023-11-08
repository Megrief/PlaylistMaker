package com.example.playlistmaker.ui.playlist_creation

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.READ_MEDIA_IMAGES
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.OnBackPressedDispatcher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.net.toUri
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentPlaylistCreationBinding
import com.example.playlistmaker.ui.main.RootActivity
import com.example.playlistmaker.ui.playlist_creation.view_model.PlaylistCreationViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistCreationFragment : Fragment() {

    private val viewModel: PlaylistCreationViewModel by viewModel()
    private var _binding: FragmentPlaylistCreationBinding? = null
    private val binding: FragmentPlaylistCreationBinding
        get() = _binding!!

    private var photoNotEmpty: Boolean = false
    private var dialog: AlertDialog? = null
    private var onBackPressedDispatcher: OnBackPressedDispatcher? = null
    private val onBackPressedCallback: OnBackPressedCallback = object : OnBackPressedCallback(true) {

        override fun handleOnBackPressed() {
            if (notEmpty()) dialog?.show() else {
                remove()
                onBackPressedDispatcher?.onBackPressed()
            }
        }
    }

    private val photoPicker = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        if (uri != null) {
            postPhoto(uri)
            viewModel.storePhoto(uri)
            photoNotEmpty = true
        }
    }

    private val permissionRequest = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
        if (isGranted) pickImage()
    }

    private var savedName: String = ""
    private var savedDescription: String = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlaylistCreationBinding.inflate(inflater, container, false)
        binding.createButton.isEnabled = false

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog = provideDialog()
        setObserver()
        configureNameInputField()
        configureDescriptionInputField()
        setOnBackPressedDispatcher()
        configurePhotoImageView()
        configureCreateButton()
        binding.toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher?.onBackPressed()
        }
    }

    override fun onResume() {
        super.onResume()
        with((requireActivity() as RootActivity).binding.bottomNav) {
            if (isVisible) visibility = View.GONE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        onBackPressedDispatcher = null
        dialog = null
    }

    override fun onPause() {
        super.onPause()
        viewModel.saveState(name = savedName, description = savedDescription, notEmpty = photoNotEmpty)
    }

    private fun setObserver() {
        viewModel.screenState.observe(viewLifecycleOwner) {
            with(binding) {
                playlistNameEt.setText(it.playlistName)
                playlistDescriptionEt.setText(it.playlistDescription)
                postPhoto(it.playlistPhoto?.toUri())
            }
        }
    }

    private fun configurePhotoImageView() {

        binding.photo.setOnClickListener {
            val permission =
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) READ_MEDIA_IMAGES
                else READ_EXTERNAL_STORAGE
            val permissionGranted = requireContext().checkSelfPermission(permission)
            if (permissionGranted == PackageManager.PERMISSION_GRANTED) {
                pickImage()
            } else {
                permissionRequest.launch(permission)
            }
        }
    }

    private fun postPhoto(uri: Uri?) {
        val cornerSizeInPx = resources.getDimensionPixelSize(R.dimen.small)
        Glide.with(binding.root)
            .load(uri)
            .placeholder(R.drawable.playlist_photo_placeholder)
            .transform(CenterCrop(), RoundedCorners(cornerSizeInPx))
            .into(binding.photo)
    }

    private fun pickImage() {
        photoPicker.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private fun configureCreateButton() {
        with(binding) {
            createButton.setOnClickListener {
                viewModel.saveState(name = savedName, description = savedDescription, notEmpty = photoNotEmpty)
                lifecycleScope.launch(Dispatchers.IO) {
                    viewModel.storePlaylist()
                }
                Toast.makeText(requireContext(), "Плейлист $savedName создан", Toast.LENGTH_SHORT).show()
                onBackPressedCallback.remove()
                onBackPressedDispatcher?.onBackPressed()
            }
        }
    }

    private fun notEmpty(): Boolean = savedName.isNotEmpty()
                || savedDescription.isNotEmpty()
                || photoNotEmpty

    private fun provideDialog(): AlertDialog = MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(R.string.dialog_title))
            .setMessage(getString(R.string.dialog_message_data_will_lost))
            .setNeutralButton(getString(R.string.cancel)) { _, _ -> }
            .setPositiveButton(getString(R.string.close)) { _, _ ->
                photoNotEmpty = false
                onBackPressedCallback.remove()
                onBackPressedDispatcher?.onBackPressed()
            }.create()

    private fun setOnBackPressedDispatcher() {
        onBackPressedDispatcher = requireActivity().onBackPressedDispatcher
        onBackPressedDispatcher?.addCallback(viewLifecycleOwner, onBackPressedCallback)
    }

    private fun configureNameInputField() {
        binding.playlistNameEt.doOnTextChanged { text, _, _, _ ->
            onColorChanged(text.isNullOrBlank(), binding.playlistName)
            binding.createButton.isEnabled = !text.isNullOrBlank()
            savedName = text.toString()
        }
    }

    private fun configureDescriptionInputField() {
        binding.playlistDescriptionEt.doOnTextChanged { text, _, _, _ ->
            onColorChanged(text.isNullOrBlank(), binding.playlistDescription)
            savedDescription = text.toString()
        }
    }

    private fun onColorChanged(isEmpty: Boolean, textInputLayout: TextInputLayout) {
        if (!isEmpty) {
            val colorStateList = resources.getColorStateList(R.color.stroke_color_filled, requireContext().theme)
            textInputLayout.setBoxStrokeColorStateList(colorStateList)
            textInputLayout.defaultHintTextColor = colorStateList
        } else {
            val colorStateList = resources.getColorStateList(R.color.playlist_creation_et_stroke_color, requireActivity().theme)
            textInputLayout.setBoxStrokeColorStateList(colorStateList)
            textInputLayout.defaultHintTextColor = colorStateList
        }
    }

}