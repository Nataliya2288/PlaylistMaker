package com.practicum.playlistmaker.data.dto

import com.google.gson.annotations.SerializedName
import com.practicum.playlistmaker.domain.Track

class TrackSearchResponse(@SerializedName("results") val tracks: ArrayList<Track>): Response() {
}