package com.practicum.playlistmaker.search.data.dto

import com.google.gson.annotations.SerializedName

data class TrackDto(
    val trackId: Int,// ID трека
    val trackName: String?, // Название композиции
    val artistName: String?,  // Имя исполнителя
    @SerializedName("trackTimeMillis") val trackTime: String?, // Продолжительность трека в формате "mm:ss"
    @SerializedName("artworkUrl100") val artworkUrl: String?, // Ссылка на изображение обложки Малый
          // Ссылка на изображение обложки Большой
    val collectionName: String?,// Название альбома
    val releaseDate: String?, // Год релиза трека -> releaseDate=2015-07-27T12:00:00Z
    val primaryGenreName: String?, // Жанр трека
    val country: String?,  // Страна исполнителя
    val previewUrl: String?  // URL отрывка трека
)