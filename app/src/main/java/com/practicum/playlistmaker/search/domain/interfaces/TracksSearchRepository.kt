package com.practicum.playlistmaker.search.domain.interfaces

import com.practicum.playlistmaker.search.domain.models.Track
import com.practicum.playlistmaker.util.Resource

interface TracksSearchRepository {
    fun searchTracks(expression: String): Resource<List<Track>>
}