package com.practicum.playlistmaker.settings.presentation

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.practicum.playlistmaker.App
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.settings.domain.interfaces.ThemeStateInteractor
import com.practicum.playlistmaker.sharing.domain.interfaces.StringStorageInteractor

class SettingsViewModel(
    val app: Application,
    private val themeStateInteractor: ThemeStateInteractor,
    private val stringStorageInteractor: StringStorageInteractor
): AndroidViewModel(app) {

    fun getThemeState(): Boolean {
        return themeStateInteractor.getThemeState()
    }

    fun saveAndChangeThemeState(state: Boolean) {
        (app as App).switchTheme(state)
        themeStateInteractor.saveThemeState(state)
    }

    fun getLinkToCourse(): String {
        return stringStorageInteractor.getString(R.string.developer)
    }

    fun getEmailMessage(): String {
        return stringStorageInteractor.getString(R.string.mail_message)
    }

    fun getEmailSubject(): String {
        return stringStorageInteractor.getString(R.string.mail_subject)
    }

    fun getPracticumOffer(): String {
        return stringStorageInteractor.getString(R.string.offer)
    }

    fun getArrayOfEmailAddresses(): Array<String> {
        return arrayOf("ameliya2288@yandex.ru")
    }

}