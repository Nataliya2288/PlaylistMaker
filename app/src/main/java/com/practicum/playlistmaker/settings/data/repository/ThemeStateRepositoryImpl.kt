package com.practicum.playlistmaker.settings.data.repository

import com.practicum.playlistmaker.settings.data.storage.ThemeStateStorage
import com.practicum.playlistmaker.settings.domain.interfaces.ThemeStateRepository

class ThemeStateRepositoryImpl (val themeStateStorage: ThemeStateStorage): ThemeStateRepository {
    override fun getThemeStateData(): Boolean {
        return themeStateStorage.getThemeStateStorage()
    }

    override fun saveThemeStateData(state: Boolean) {
        themeStateStorage.saveThemeStateStorage(state)
    }
}
