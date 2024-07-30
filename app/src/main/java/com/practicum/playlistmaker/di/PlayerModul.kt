package com.practicum.playlistmaker.di

import android.media.MediaPlayer
import com.practicum.playlistmaker.player.data.repository.AudioPlayerRepositoryImpl
import com.practicum.playlistmaker.player.domain.interactors.AudioPlayerInteractorImpl
import com.practicum.playlistmaker.player.domain.interfaces.AudioPlayerInteractor
import com.practicum.playlistmaker.player.domain.interfaces.AudioPlayerRepository
import com.practicum.playlistmaker.player.presentation.PlayerViewModel
import com.practicum.playlistmaker.search.domain.models.Track

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.parameter.parametersOf
import org.koin.dsl.module

val playerModule = module {
    // Создание экземпляра MediaPlayer
    factory { MediaPlayer() }

    // Определение AudioPlayerRepository
    factory<AudioPlayerRepository> {
        AudioPlayerRepositoryImpl(mediaPlayer = get())
    }

    // Определение AudioPlayerInteractor с правильной передачей параметров
    factory<AudioPlayerInteractor> { (track: Track) ->
        AudioPlayerInteractorImpl(audioPlayerRepository = get())
    }

    // Определение ViewModel с правильным использованием параметров
    viewModel { (track: Track) ->
        PlayerViewModel(track = track, audioPlayerInteractor = get { parametersOf(track) })
    }
}