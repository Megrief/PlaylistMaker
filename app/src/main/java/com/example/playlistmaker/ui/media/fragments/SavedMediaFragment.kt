package com.example.playlistmaker.ui.media.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.playlistmaker.R
import com.example.playlistmaker.ui.media.fragments.view_models.SavedMediaViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SavedMediaFragment : Fragment() {

    private val viewModel: SavedMediaViewModel by viewModel()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_saved_media, container, false)
    }

    companion object {
        fun newInstance(): SavedMediaFragment {
            return SavedMediaFragment()
        }
    }
}