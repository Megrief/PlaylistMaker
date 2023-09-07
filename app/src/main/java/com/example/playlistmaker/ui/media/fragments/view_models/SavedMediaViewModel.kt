package com.example.playlistmaker.ui.media.fragments.view_models

import androidx.lifecycle.ViewModel
import com.example.playlistmaker.domain.entity.Track
import com.example.playlistmaker.domain.storage.use_cases.GetDataUseCase
import com.example.playlistmaker.domain.storage.use_cases.StoreDataUseCase

class SavedMediaViewModel(
    private val storeTrackListUseCase: StoreDataUseCase<List<Track>>,
    private val getTrackListUseCase: GetDataUseCase<List<Track>>,
    private val storeTrackUseCase: StoreDataUseCase<Track>
) : ViewModel() {


}