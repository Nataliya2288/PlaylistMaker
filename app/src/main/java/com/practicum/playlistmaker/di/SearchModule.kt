package com.practicum.playlistmaker.di

import android.content.Context
import com.practicum.playlistmaker.BASE_URL
import com.practicum.playlistmaker.SHARED_PREFERENCES
import com.practicum.playlistmaker.search.data.network.ITunesApi
import com.practicum.playlistmaker.search.data.network.NetworkClient
import com.practicum.playlistmaker.search.data.network.RetrofitNetworkClient
import com.practicum.playlistmaker.search.data.repository.HistoryTrackRepositorySHImpl
import com.practicum.playlistmaker.search.data.repository.TracksSearchSearchRepositoryImpl
import com.practicum.playlistmaker.search.data.storage.TrackSearchHistoryStorage
import com.practicum.playlistmaker.search.data.storage.TrackSearchHistoryStorageSharedPrefs
import com.practicum.playlistmaker.search.domain.interactors.TrackHistoryInteractorImpl
import com.practicum.playlistmaker.search.domain.interactors.TracksSearchSearchInteractorImpl
import com.practicum.playlistmaker.search.domain.interfaces.HistoryTrackRepositorySH
import com.practicum.playlistmaker.search.domain.interfaces.TrackHistoryInteractor
import com.practicum.playlistmaker.search.domain.interfaces.TracksSearchInteractor
import com.practicum.playlistmaker.search.domain.interfaces.TracksSearchRepository
import com.practicum.playlistmaker.search.presentation.SearchingViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val searchModule = module {
    single {
        androidContext().getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE)
    }

    single<TrackSearchHistoryStorage> {
        TrackSearchHistoryStorageSharedPrefs(sharedPreferences = get())
    }

    single<HistoryTrackRepositorySH> {
        HistoryTrackRepositorySHImpl(trackSearchHistoryStorage = get())
    }

    single<ITunesApi> {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ITunesApi::class.java)
    }

    single<NetworkClient> {
        RetrofitNetworkClient(context = androidContext(), trackService = get())
    }

    single<TracksSearchRepository> {
        TracksSearchSearchRepositoryImpl(networkClient = get())
    }

    factory<TrackHistoryInteractor> {
        TrackHistoryInteractorImpl(historyTrackRepositorySH = get())
    }

    factory<TracksSearchInteractor> {
        TracksSearchSearchInteractorImpl(tracksSearchRepository = get())
    }

    viewModel {
        SearchingViewModel(tracksSearchInteractor = get(), trackHistoryInteractor = get())
    }

}