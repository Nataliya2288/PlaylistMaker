package com.practicum.playlistmaker.settings.domain.interactors

import com.practicum.playlistmaker.settings.domain.interfaces.ThemeStateInteractor
import com.practicum.playlistmaker.settings.domain.interfaces.ThemeStateRepository

class ThemeStateInteractorImpl(val themeStateRepository: ThemeStateRepository):
    ThemeStateInteractor {

    override fun getThemeState(): Boolean {
        return themeStateRepository.getThemeStateData()
    }

    override fun saveThemeState(state: Boolean) {
        themeStateRepository.saveThemeStateData(state)
    }
}