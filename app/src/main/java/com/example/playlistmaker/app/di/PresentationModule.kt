package com.example.playlistmaker.app.di

import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
import com.example.playlistmaker.domain.entities.Track
import com.example.playlistmaker.domain.settings.entity.ThemeFlag
import com.example.playlistmaker.domain.storage.use_cases.GetDataUseCase
import com.example.playlistmaker.domain.storage.use_cases.StoreDataUseCase
import com.example.playlistmaker.ui.audioplayer.view_model.AudioplayerViewModel
import com.example.playlistmaker.ui.audioplayer.view_model.player.Player
import com.example.playlistmaker.ui.search.view_model.SearchRequestDebouncer
import com.example.playlistmaker.ui.search.view_model.SearchViewModel
import com.example.playlistmaker.ui.settings.utils.SingleLiveEvent
import com.example.playlistmaker.ui.settings.view_model.DarkThemeState
import com.example.playlistmaker.ui.settings.view_model.SettingsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

val presentationModule = module {

    viewModel {
        val getTrackUseCase: GetDataUseCase<Track?> = get(named("GetTrackUseCase"))
        AudioplayerViewModel(getDataUseCase =  getTrackUseCase)
    }

    viewModel {
        val searchUseCase: GetDataUseCase<List<Track>?> = get(named("SearchUseCase"))
        val storeTrackUseCase: StoreDataUseCase<Track> = get(named("StoreTrackUseCase"))
        val storeTrackListUseCase: StoreDataUseCase<List<Track>> = get(named("StoreTrackListUseCase"))
        val getTrackListUseCase: GetDataUseCase<List<Track>> = get(named("GetTrackListUseCase"))
        SearchViewModel(
            searchUseCase = searchUseCase,
            storeTrackUseCase = storeTrackUseCase,
            storeTrackListUseCase = storeTrackListUseCase,
            getTrackListUseCase = getTrackListUseCase
        )
    }

    viewModel {
        val getThemeFlagUseCase: GetDataUseCase<ThemeFlag?> = get(named("GetThemeFlagUseCase"))
        SettingsViewModel(
            getThemeFlagUseCase = getThemeFlagUseCase,
            switchThemeUseCase = get(),
            sharingRepository = get()
        )
    }

    factory {
        SingleLiveEvent<DarkThemeState>()
    }

    factory {
        Handler(Looper.getMainLooper())
    }

    factory {
        SearchRequestDebouncer(get())
    }

    factory { params ->
        Player(
            url = params[0],
            onPrepared = params[1],
            onCompeted = params[1],
            player = get()
        )
    }

    factory {
        MediaPlayer()
    }


}