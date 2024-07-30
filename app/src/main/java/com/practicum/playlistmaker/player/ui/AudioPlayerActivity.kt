package com.practicum.playlistmaker.player.ui
import android.content.Intent
import android.os.Build
import android.os.Build.VERSION.SDK_INT
import android.os.Build.VERSION_CODES.TIRAMISU
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.TypedValue
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.KEY_FOR_PLAYER
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.player.presentation.PlayerState
import com.practicum.playlistmaker.player.presentation.PlayerViewModel
import com.practicum.playlistmaker.search.domain.models.Track
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import java.io.Serializable

private const val CORNERRADIUS_DP = 8f
private const val TIME = "time" // Тег для сохранения позиции таймера
class AudioPlayerActivity : AppCompatActivity() {
    private lateinit var backArrow: ImageView
    private lateinit var coverImage: ImageView
    private lateinit var trackName: TextView
    private lateinit var artistName: TextView
    private lateinit var duration: TextView
    private lateinit var collectionName: TextView
    private lateinit var year: TextView
    private lateinit var genre: TextView
    private lateinit var country: TextView
    private lateinit var playButton: ImageView
    private lateinit var durationInTime: TextView
    private lateinit var viewModel: PlayerViewModel
    companion object {
        private const val REFRESH_PROGRESS_DELAY = 300L
    }
    private var playerState = PlayerState.STATE_DEFAULT


    private lateinit var currentTime: String
    private val handler = Handler(Looper.getMainLooper())
    private lateinit var runnable: Runnable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.setContentView(R.layout.activity_audio_player)

            val backButton = findViewById<ImageView>(R.id.backArrowPlaylist)
        backArrow = findViewById(R.id.backArrowPlaylist)
        coverImage = findViewById(R.id.coverMax)
        trackName = findViewById(R.id.trackName)
        artistName = findViewById(R.id.artistName)
        duration = findViewById(R.id.durationName)
        collectionName = findViewById(R.id.albumName)
        year = findViewById(R.id.yearName)
        genre = findViewById(R.id.genreName)
        country = findViewById(R.id.countryName)
        playButton = findViewById(R.id.playButton)
        durationInTime = findViewById(R.id.durationInTime)

        val track = intent.getSerializable(KEY_FOR_PLAYER, Track::class.java)
        val vModel: PlayerViewModel by viewModel {
            parametersOf(track)
        }
        viewModel = vModel

        currentTime = getString(R.string.player_start_position)
        if (savedInstanceState != null) {
            currentTime =
                savedInstanceState.getString(TIME, getString(R.string.player_start_position))
            durationInTime.text = currentTime
        }
        screenPreparation(track)    // Заполнение экрана

        runnable = Runnable {
            if (playerState == PlayerState.STATE_PLAYING) {
                durationInTime.text = viewModel.currentPosition()
            }
            handler.postDelayed(runnable, REFRESH_PROGRESS_DELAY)
        }
        handler.postDelayed(runnable, REFRESH_PROGRESS_DELAY)
        // Нажатие кнопки Назад закрывает AudioPlayer
        backButton.setOnClickListener {
            finish()
        }
// Реакция на нажатие кнопки Play
        playButton.setOnClickListener {
            viewModel.changeStatePlayerAfterClick()
        }
        // Получение данных от PlayerViewModel
        viewModel.getStatePlayerLiveData().observe(this) { newState ->
            playerState = newState
            playbackControl()
        }
    }
    // После подготовки плеера
    private fun afterPreparingPlayer () {
        playButton.isEnabled = true
        playButton.setImageResource(R.drawable.play_button)
        // Выставляем прогресс воспроизведения 00:00
        durationInTime.text = getString(R.string.player_start_position)
        currentTime = getString(R.string.player_start_position)
        durationInTime.text = currentTime
    }
    // Во время подготовки плеера
    private fun duringPreparation () {
        playButton.isEnabled = false
    }
    // Включение воспроизведения
    private fun startPlayer() {
        viewModel.start()
        playButton.setImageResource(R.drawable.pause_button)
        viewModel.updateStatePlayerLiveData(PlayerState.STATE_PLAYING)
    }
    // Пауза в воспроизведении
    private fun pausePlayer() {
        viewModel.pause()
        playButton.setImageResource(R.drawable.play_button)
        viewModel.updateStatePlayerLiveData(PlayerState.STATE_PAUSED)
    }
    // Выбор действия при нажатии кнопки Play
    private fun playbackControl() {
        when(playerState) {
            PlayerState.STATE_ERROR -> showError()
            PlayerState.STATE_NO_ALBUM_NAME -> noCollectionName()
            PlayerState.STATE_PLAYING -> startPlayer()
            PlayerState.STATE_PAUSED -> pausePlayer()
            PlayerState.STATE_PREPARED -> afterPreparingPlayer()
            PlayerState.STATE_DEFAULT -> duringPreparation()
            else -> {}
        }
    }
    override fun onPause() {
        super.onPause()
        pausePlayer()
    }
    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(runnable)
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
            .into(coverImage)
        // Заполняем поля:
        trackName.text = track?.trackName   // Назввание трека
        artistName.text = track?.artistName  // Имя исполнителя
        duration.text = track?.trackTime  // Продолжительность трека
        collectionName.text = track?.collectionName // Название альбома
        year.text = track?.releaseDate?.subSequence(0,4)  // Год выхода (первые 4-е символа строки)
        genre.text = track?.primaryGenreName          // Жанр трека
        country.text = track?.country                            // Страна исполнителя
    }
    // Если имя альбома пустое
    private fun noCollectionName (){
        collectionName.isVisible = false

    }
    // Состояние ошибки, если передан track == null
    private fun showError () {
        Toast.makeText(this@AudioPlayerActivity, getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(TIME, durationInTime.text.toString())
    }
}


