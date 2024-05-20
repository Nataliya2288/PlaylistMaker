package com.practicum.playlistmaker.domain.interfaces

import com.practicum.playlistmaker.domain.Track

interface TracksSearchInteractor {

    fun searchTracks(expression: String, tracksConsumer: TracksConsumer )

    interface TracksConsumer {
        fun consume(foundTracks: List<Track>?, isFailed: Boolean?)
    }

}
