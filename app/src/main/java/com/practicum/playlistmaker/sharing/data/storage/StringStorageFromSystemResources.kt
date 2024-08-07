package com.practicum.playlistmaker.sharing.data.storage

import android.content.Context


class StringStorageFromSystemResources(private val context: Context): StringStorage {
    override fun getStringFromStorage(id: Int): String {
        return context.getString(id)
    }
}