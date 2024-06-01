package com.practicum.playlistmaker.search.ui.models

import com.practicum.playlistmaker.search.domain.models.Track

data class TracksState(
    val tracks: List<Track>,
    val isLoading: Boolean,
    val isFailed: Boolean?
)