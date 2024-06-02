package com.practicum.playlistmaker.settings.data.storage

import android.content.Context
import com.practicum.playlistmaker.KEY_FOR_APP_THEME
import com.practicum.playlistmaker.SHARED_PREFERENCES

class ThemeStateStorageSharedPrefs(context: Context): ThemeStateStorage {

    private val sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE)

    override fun getThemeStateStorage(): Boolean {
        return sharedPreferences.getBoolean(KEY_FOR_APP_THEME, false)
    }

    override fun saveThemeStateStorage(state: Boolean) {
        sharedPreferences.edit()
            .putBoolean(KEY_FOR_APP_THEME, state)
            .apply()
    }
}