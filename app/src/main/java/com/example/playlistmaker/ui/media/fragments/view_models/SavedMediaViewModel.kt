package com.example.playlistmaker.ui.media.fragments.view_models

import androidx.lifecycle.ViewModel
import com.example.playlistmaker.domain.db.use_cases.DeleteItemUseCase
import com.example.playlistmaker.domain.db.use_cases.GetItemByIdUseCase
import com.example.playlistmaker.domain.entity.Track
import com.example.playlistmaker.domain.storage.use_cases.GetDataUseCase
import com.example.playlistmaker.domain.storage.use_cases.StoreDataUseCase

class SavedMediaViewModel(
    private val deleteItemUseCase: DeleteItemUseCase<Track>,
    private val storeTrackUseCase: StoreDataUseCase<Track>,
    private val getTrackByIdUseCase: GetItemByIdUseCase<Track>,
    private val getFavouriteUseCase: GetDataUseCase<List<Track>>
) : ViewModel() {


}