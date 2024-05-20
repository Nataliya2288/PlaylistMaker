package com.practicum.playlistmaker.presentation.ui.main

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.practicum.playlistmaker.presentation.ui.library.LibraryActivity
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.presentation.ui.setting.SettingActivity
import com.practicum.playlistmaker.presentation.ui.search.SearchActivity

class MainActivity() : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val buttonSearch = findViewById<Button>(R.id.search)
        val buttonLibrary = findViewById<Button>(R.id.library)
        val buttonSetting = findViewById<Button>(R.id.settings)

        buttonSearch.setOnClickListener {
            val displayIntent = Intent(this, SearchActivity::class.java)
            startActivity(displayIntent)
        }

        buttonLibrary.setOnClickListener {
            val displayIntent = Intent(this, LibraryActivity::class.java)
            startActivity(displayIntent)
        }

        buttonSetting.setOnClickListener {
            val displayIntent = Intent(this, SettingActivity::class.java)
            startActivity(displayIntent)
        }
    }
}



