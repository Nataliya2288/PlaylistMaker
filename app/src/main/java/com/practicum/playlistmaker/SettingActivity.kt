package com.practicum.playlistmaker

import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate

class SettingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)
        val buttonThemes = findViewById<TextView>(R.id.theme_switcher)
        val buttonShare = findViewById<TextView>(R.id.share_app)
        val buttonSupport = findViewById<TextView>(R.id.write_in_support)
        val buttonAgreement = findViewById<TextView>(R.id.user_agreement)
        val buttonSettingsBack = findViewById<View>(R.id.settings_back)
        buttonThemes.setOnClickListener {
            when (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
                Configuration.UI_MODE_NIGHT_NO -> {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                } // ночная тема неактивна, вы ипользуете светлую тему
                Configuration.UI_MODE_NIGHT_YES -> {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                } // ночная тема активна, вы используете темную тему
            }
        }
        buttonShare.setOnClickListener {
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.developer))
            shareIntent.type = "*/*"
            startActivity(shareIntent)
        }
        buttonSupport.setOnClickListener {
            val supportIntent = Intent(Intent.ACTION_SENDTO)
            supportIntent.data = Uri.parse("mailto:")
            supportIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.my_email)))
            supportIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.mail_subject))
            supportIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.mail_message))
            startActivity(supportIntent)
        }
        buttonAgreement.setOnClickListener {
            val agreementIntent =
                Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.offer)))
            startActivity(agreementIntent)
        }
        buttonSettingsBack.setOnClickListener {
            finish()
        }
    }
}

