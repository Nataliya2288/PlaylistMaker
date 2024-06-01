package com.practicum.playlistmaker.util

import android.content.Context
import com.practicum.playlistmaker.player.data.repository.AudioPlayerRepositoryImpl
import com.practicum.playlistmaker.player.domain.interactors.AudioPlayerInteractorImpl
import com.practicum.playlistmaker.player.domain.interfaces.AudioPlayerInteractor
import com.practicum.playlistmaker.player.domain.interfaces.AudioPlayerRepository
import com.practicum.playlistmaker.player.domain.models.PlayerTrack
import com.practicum.playlistmaker.search.data.network.RetrofitNetworkClient
import com.practicum.playlistmaker.search.data.repository.HistoryTrackRepositorySHImpl
import com.practicum.playlistmaker.search.data.repository.TracksSearchSearchRepositoryImpl
import com.practicum.playlistmaker.search.data.storage.TrackSearchHistoryStorageSharedPrefs
import com.practicum.playlistmaker.search.domain.interactors.TrackHistoryInteractorImpl
import com.practicum.playlistmaker.search.domain.interactors.TracksSearchSearchInteractorImpl
import com.practicum.playlistmaker.search.domain.interfaces.HistoryTrackRepositorySH
import com.practicum.playlistmaker.search.domain.interfaces.TrackHistoryInteractor
import com.practicum.playlistmaker.search.domain.interfaces.TracksSearchInteractor
import com.practicum.playlistmaker.search.domain.interfaces.TracksSearchRepository
import com.practicum.playlistmaker.settings.data.repository.ThemeStateRepositoryImpl
import com.practicum.playlistmaker.settings.data.storage.ThemeStateStorageSharedPrefs
import com.practicum.playlistmaker.settings.domain.interactors.ThemeStateInteractorImpl
import com.practicum.playlistmaker.settings.domain.interfaces.ThemeStateInteractor
import com.practicum.playlistmaker.settings.domain.interfaces.ThemeStateRepository
import com.practicum.playlistmaker.sharing.data.repository.StringStorageRepositoryImpl
import com.practicum.playlistmaker.sharing.data.storage.StringStorageFromSystemResources
import com.practicum.playlistmaker.sharing.domain.interactors.StringStorageInteractorImpl
import com.practicum.playlistmaker.sharing.domain.interfaces.StringStorageInteractor
import com.practicum.playlistmaker.sharing.domain.interfaces.StringStorageRepository

object Creator {
    private fun getThemeStateRepository(context: Context): ThemeStateRepository {
        return ThemeStateRepositoryImpl(ThemeStateStorageSharedPrefs(context))
    }

    fun provideThemeStateInteractor(context: Context): ThemeStateInteractor {
        return ThemeStateInteractorImpl(getThemeStateRepository(context))
    }

    private fun getStringStorageRepository(context: Context): StringStorageRepository {
        return StringStorageRepositoryImpl(StringStorageFromSystemResources(context))
    }

    fun provideStringStorageInteractor(context: Context): StringStorageInteractor {
        return StringStorageInteractorImpl(getStringStorageRepository(context))
    }

    private fun getTracksSearchRepository(context: Context): TracksSearchRepository {
        return TracksSearchSearchRepositoryImpl(RetrofitNetworkClient(context))
    }

    fun provideTracksSearchInteractor(context: Context): TracksSearchInteractor {
        return TracksSearchSearchInteractorImpl(getTracksSearchRepository(context))
    }

    private fun getHistoryTrackRepositorySH(context: Context): HistoryTrackRepositorySH {
        return HistoryTrackRepositorySHImpl(TrackSearchHistoryStorageSharedPrefs(context))
    }

    fun provideTrackHistoryInteractor(context: Context): TrackHistoryInteractor {
        return TrackHistoryInteractorImpl(getHistoryTrackRepositorySH(context))
    }



    private fun getAudioPlayerRepository(): AudioPlayerRepository {
        return AudioPlayerRepositoryImpl()
    }

    fun provideAudioPlayerInteractor(playerTrack: PlayerTrack): AudioPlayerInteractor {
        return AudioPlayerInteractorImpl(playerTrack, getAudioPlayerRepository())
    }


}