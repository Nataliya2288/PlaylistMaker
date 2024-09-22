package com.practicum.playlistmaker.search.domain.interfaces

import com.practicum.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface TracksSearchInteractor {
    fun searchTracks(expression: String): Flow<Pair<List<Track>?, Boolean?>>

}
