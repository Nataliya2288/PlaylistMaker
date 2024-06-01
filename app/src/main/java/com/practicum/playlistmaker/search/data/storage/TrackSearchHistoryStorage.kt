package com.practicum.playlistmaker.search.data.storage

import com.practicum.playlistmaker.search.data.dto.TrackDto

interface TrackSearchHistoryStorage {
    fun getTracksFromStorage(): Array<TrackDto>

    fun saveTracksToStorage(tracks: ArrayList<TrackDto>)
}