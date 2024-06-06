package com.practicum.playlistmaker.di

import android.app.Application
import com.practicum.playlistmaker.settings.data.repository.ThemeStateRepositoryImpl
import com.practicum.playlistmaker.settings.data.storage.ThemeStateStorage
import com.practicum.playlistmaker.settings.data.storage.ThemeStateStorageSharedPrefs
import com.practicum.playlistmaker.settings.domain.interactors.ThemeStateInteractorImpl
import com.practicum.playlistmaker.settings.domain.interfaces.ThemeStateInteractor
import com.practicum.playlistmaker.settings.domain.interfaces.ThemeStateRepository
import com.practicum.playlistmaker.settings.presentation.SettingsViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val settingsModule = module {
    single<ThemeStateStorage> {
        ThemeStateStorageSharedPrefs(sharedPreferences = get())
    }

    single<ThemeStateRepository> {
        ThemeStateRepositoryImpl(themeStateStorage = get())
    }

    factory<ThemeStateInteractor> {
        ThemeStateInteractorImpl(themeStateRepository = get())
    }

    single {
        androidContext().applicationContext as Application
    }

    viewModel {
        SettingsViewModel(app = get(), themeStateInteractor = get(), stringStorageInteractor = get())
    }

}