package com.practicum.playlistmaker.di

import com.practicum.playlistmaker.sharing.data.repository.StringStorageRepositoryImpl
import com.practicum.playlistmaker.sharing.data.storage.StringStorage
import com.practicum.playlistmaker.sharing.data.storage.StringStorageFromSystemResources
import com.practicum.playlistmaker.sharing.domain.interactors.StringStorageInteractorImpl
import com.practicum.playlistmaker.sharing.domain.interfaces.StringStorageInteractor
import com.practicum.playlistmaker.sharing.domain.interfaces.StringStorageRepository
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val sharingModule = module {
    single<StringStorage> {
        StringStorageFromSystemResources(context = androidContext())
    }

    single<StringStorageRepository> {
        StringStorageRepositoryImpl(stringStorage = get())
    }

    factory<StringStorageInteractor> {
        StringStorageInteractorImpl(stringStorageRepository = get())
    }
}