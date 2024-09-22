package com.practicum.playlistmaker.search.domain.interactors

import com.practicum.playlistmaker.search.domain.models.Track
import com.practicum.playlistmaker.search.domain.interfaces.TracksSearchInteractor
import com.practicum.playlistmaker.search.domain.interfaces.TracksSearchRepository
import com.practicum.playlistmaker.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.concurrent.Executors


class TracksSearchSearchInteractorImpl(private val tracksSearchRepository: TracksSearchRepository) :
    TracksSearchInteractor {
    override fun searchTracks(expression: String): Flow<Pair<List<Track>?, Boolean?>> {
        return tracksSearchRepository.searchTracks(expression).map { result ->
            when(result) {
                is Resource.Success -> {
                    Pair(result.data, null)
                }
                is Resource.Error -> {
                    Pair(null, result.isFailed)
                }
            }
        }
    }
}


