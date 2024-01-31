package com.practicum.playlistmaker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import java.util.Objects

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val buttonSearch = findViewById<LinearLayout>(R.id.search_button) as Button
        val imageClickListener: View.OnClickListener = object : View.OnClickListener{
            override fun onClick(v: View?) {
                // Добавьте код для перехода на другой экран, связанный с поиском
                val intent = Intent(this, SearchActivity::class.java)
                startActivity(intent)
                finish() // Завершить текущий экран


            }
        }
        buttonSearch.setOnClickListener (imageClickListener)

        val buttonLibrary = findViewById<ImageView>(R.id.library_button) as Button
        buttonLibrary.setOnClickListener {
            // Добавьте код для перехода на экран с библиотекой
            val intent = Intent(this, LibraryActivity::class.java)
            startActivity(intent)
            finish() // Завершить текущий экран
        }

        val buttonSetting = findViewById<ImageView>(R.id.setting_button) as Button
        buttonSetting.setOnClickListener {
            // Добавьте код для перехода на экран настроек
            val intent = Intent(this, SettingActivity::class.java)
            startActivity(intent)
            finish() // Завершить текущий экран
        }
    }

    private fun startActivity(intent: Unit) {

    }

    private fun Intent(onClickListener: View.OnClickListener, java: Class<SearchActivity>) {

    }
}
