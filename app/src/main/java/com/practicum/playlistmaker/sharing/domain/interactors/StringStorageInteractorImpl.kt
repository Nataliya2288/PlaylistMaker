package com.practicum.playlistmaker.sharing.domain.interactors

import com.practicum.playlistmaker.sharing.domain.interfaces.StringStorageInteractor
import com.practicum.playlistmaker.sharing.domain.interfaces.StringStorageRepository

class StringStorageInteractorImpl(val stringStorageRepository: StringStorageRepository):
    StringStorageInteractor {
    override fun getString(id: Int): String {
        return stringStorageRepository.getStringById(id)
    }
}
