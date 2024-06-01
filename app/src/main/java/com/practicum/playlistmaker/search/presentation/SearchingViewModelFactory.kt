package com.practicum.playlistmaker.search.presentation

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.practicum.playlistmaker.util.Creator

class SearchingViewModelFactory (context: Context): ViewModelProvider.Factory {

    private val tracksInteractor = Creator.provideTracksSearchInteractor(context)
    private val trackHistoryInteractor = Creator.provideTrackHistoryInteractor(context)

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SearchingViewModel(tracksInteractor, trackHistoryInteractor) as T
    }
}