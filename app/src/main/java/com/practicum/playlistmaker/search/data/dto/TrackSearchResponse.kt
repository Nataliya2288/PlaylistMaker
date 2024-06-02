package com.practicum.playlistmaker.search.data.dto

import com.google.gson.annotations.SerializedName
import com.practicum.playlistmaker.search.domain.models.Track

class TrackSearchResponse(@SerializedName("results") val tracks: ArrayList<Track>): Response() {
}