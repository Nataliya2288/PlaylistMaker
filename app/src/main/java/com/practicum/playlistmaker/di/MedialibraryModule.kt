package com.practicum.playlistmaker.di

import com.practicum.playlistmaker.medialibrary.presentation.MedialibraryFavouritesViewModel
import com.practicum.playlistmaker.medialibrary.presentation.MedialibraryPlaylistsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val medialibraryModule = module {

    viewModel {
        MedialibraryFavouritesViewModel()
    }

    viewModel {
        MedialibraryPlaylistsViewModel()
    }
}