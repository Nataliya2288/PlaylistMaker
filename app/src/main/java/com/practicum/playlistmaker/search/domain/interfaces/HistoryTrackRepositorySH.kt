package com.practicum.playlistmaker.search.domain.interfaces

import com.practicum.playlistmaker.search.domain.models.Track

interface HistoryTrackRepositorySH {
    fun getTrackListFromSH(): Array<Track>
    fun saveTrackListToSH(historyList: ArrayList<Track>)
}