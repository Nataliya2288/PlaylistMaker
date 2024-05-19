package com.practicum.playlistmaker.data

import com.practicum.playlistmaker.data.dto.TrackSearchRequest
import com.practicum.playlistmaker.data.dto.TrackSearchResponse
import com.practicum.playlistmaker.data.network.NetworkClient
import com.practicum.playlistmaker.domain.Track
import com.practicum.playlistmaker.domain.interfaces.TracksSearchRepository
import com.practicum.playlistmaker.util.Resource

class TracksSearchSearchRepositoryImpl(private val networkClient: NetworkClient):
    TracksSearchRepository {

    override fun searchTracks(expression: String): Resource<List<Track>> {
        val response = networkClient.doRequest(TrackSearchRequest(expression))

        return when (response.resultCode) {
            -1 -> {
                Resource.Error(isFailed = false)
            }
            200 -> {
                Resource.Success((response as TrackSearchResponse).tracks.map {
                    Track(
                        trackId = it.trackId,
                        trackName = it.trackName,
                        artistName = it.artistName,
                        trackTime = it.trackTime,
                        artworkUrl = it.artworkUrl,
                        collectionName = it.collectionName,
                        releaseDate = it.releaseDate,
                        primaryGenreName = it.primaryGenreName,
                        country = it.country,
                        previewUrl = it.previewUrl
                    )
                })
            }
            else -> {
                Resource.Error(isFailed = true)
            }
        }
    }
}
