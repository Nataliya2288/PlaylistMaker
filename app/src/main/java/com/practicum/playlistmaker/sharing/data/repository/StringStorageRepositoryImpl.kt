package com.practicum.playlistmaker.sharing.data.repository

import com.practicum.playlistmaker.sharing.data.storage.StringStorage
import com.practicum.playlistmaker.sharing.domain.interfaces.StringStorageRepository

class StringStorageRepositoryImpl(val stringStorage: StringStorage): StringStorageRepository {
    override fun getStringById(id: Int): String {
        return stringStorage.getStringFromStorage(id)
    }
}