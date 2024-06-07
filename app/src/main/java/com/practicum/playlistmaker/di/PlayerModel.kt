package com.practicum.playlistmaker.di

import android.media.MediaPlayer
import com.practicum.playlistmaker.player.data.repository.AudioPlayerRepositoryImpl
import com.practicum.playlistmaker.player.domain.interactors.AudioPlayerInteractorImpl
import com.practicum.playlistmaker.player.domain.interfaces.AudioPlayerInteractor
import com.practicum.playlistmaker.player.domain.interfaces.AudioPlayerRepository
import com.practicum.playlistmaker.player.domain.models.PlayerTrack
import com.practicum.playlistmaker.player.presentation.PlayerViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.parameter.parametersOf
import org.koin.dsl.module

val playerModule = module {
    factory { MediaPlayer() }

    factory <AudioPlayerRepository> {
        AudioPlayerRepositoryImpl(mediaPlayer = get())
    }

    factory<AudioPlayerInteractor> { (playerTrack: PlayerTrack) ->
        AudioPlayerInteractorImpl(playerTrack = playerTrack, audioPlayerRepository = get())
    }

    viewModel { (playerTrack: PlayerTrack) ->
        PlayerViewModel(playerTrack = playerTrack, audioPlayerInteractor = get { parametersOf(playerTrack) })
    }

}