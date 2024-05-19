package com.practicum.playlistmaker.domain.interfaces

import com.bumptech.glide.load.engine.Resource
import com.practicum.playlistmaker.domain.Track

interface TracksSearchRepository {fun searchTracks(expression: String): com.practicum.playlistmaker.util.Resource<List<Track>>
}
