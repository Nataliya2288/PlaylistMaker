package com.practicum.playlistmaker

enum class TrackSearchStatus {
    Success  // Успешно
    ,
    NoDataFound // Ничего не нашлось
    ,
    ConnectionError // Ошибка
    ,
    ShowHistory // История
    ,
    Empty // Пусто
}