package com.practicum.playlistmaker.player.ui
import android.content.Intent
import android.os.Build
import android.os.Build.VERSION.SDK_INT
import android.os.Build.VERSION_CODES.TIRAMISU
import android.os.Bundle
import android.util.TypedValue
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.KEY_FOR_PLAYER
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.ActivityAudioPlayerBinding
import com.practicum.playlistmaker.player.presentation.PlayerState
import com.practicum.playlistmaker.player.presentation.PlayerViewModel
import com.practicum.playlistmaker.search.domain.models.Track
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import java.io.Serializable


private const val CORNERRADIUS_DP = 8f
private const val TIME = "time"                     // Тег для сохранения позиции таймера
class AudioPlayerActivity : AppCompatActivity() {

    private lateinit var playerState: PlayerState
    private lateinit var currentTime: String
    private lateinit var viewModel: PlayerViewModel
    private lateinit var binding: ActivityAudioPlayerBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAudioPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val track: Track? = intent.getSerializable(KEY_FOR_PLAYER, Track::class.java)

        val vModel: PlayerViewModel by viewModel { parametersOf(track) }
        viewModel = vModel
        if(savedInstanceState != null) {
            currentTime = savedInstanceState.getString(TIME, getString(R.string.player_start_position))
            binding.durationInTime.text = currentTime
        }
        screenPreparation(track)    // Заполнение экрана
        // Нажатие кнопки Назад закрывает AudioPlayer
        binding.backArrowPlaylist.setOnClickListener {
            finish()
        }
        // Реакция на нажатие кнопки Play
        binding.playButton.setOnClickListener {
            viewModel.changeStatePlayerAfterClick()
        }
        // Получение данных от PlayerViewModel
        viewModel.getStatePlayerLiveData().observe(this) { newState ->
            playerState = newState
            playbackControl()
        }
    }
    private fun playbackControl() {
        binding.playButton.isEnabled = playerState.isPlayButtonEnabled
        binding.playButton.setImageResource(if(playerState.buttonIcon == "PLAY") R.drawable.play_button else R.drawable.pause_button)
        playerState.progress.also { binding.durationInTime.text = it }
    }

    fun <T : Serializable?> Intent.getSerializable(key: String, m_class: Class<T>): T {
        return if (SDK_INT >= TIRAMISU)
            this.getSerializableExtra(key, m_class)!!
        else
            this.getSerializableExtra(key) as T
    }
    private fun screenPreparation(track: Track?) {
        // Выводим обложку альбома
        Glide.with(this)
            .load(track?.artworkUrl)
            .placeholder(R.drawable.track_placeholder_max)
            .centerCrop()
            .transform(
                RoundedCorners(
                    TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP,
                        CORNERRADIUS_DP,
                        this.resources.displayMetrics
                    ).toInt()
                )
            )
            .into(binding.coverMax)
        // Заполняем поля:
        binding.trackName.text = track?.trackName                        // Назввание трека
        binding.artistName.text = track?.artistName                      // Имя исполнителя
        binding.durationInTime.text = track?.trackTime                         // Продолжительность трека
        if (track?.collectionName?.isNotEmpty() == true) {
            binding.albumName.text = track.collectionName           // Название альбома
        } else {
            noCollectionName()
        }
        binding.yearName.text = track?.releaseDate?.subSequence(0,4)  // Год выхода (первые 4-е символа строки)
        binding.genreName.text = track?.primaryGenreName          // Жанр трека
        binding.countryName.text = track?.country                            // Страна исполнителя
        binding.playButton.isEnabled = false                             // При загрузке делаем кнопку Play недоступной до инициализации плейера
    }
    // Если имя альбома пустое
    private fun noCollectionName (){
        binding.albumName.isVisible = false


    }
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(TIME, binding.durationInTime.text.toString())
    }

    override fun onPause() {
        super.onPause()
        viewModel.pause()
    }
}


