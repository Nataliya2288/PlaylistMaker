package com.practicum.playlistmaker.player.presentation
import android.media.MediaPlayer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.practicum.playlistmaker.player.domain.interfaces.AudioPlayerInteractor
import com.practicum.playlistmaker.search.domain.models.Track



class PlayerViewModel(
    private val audioPlayerInteractor: AudioPlayerInteractor,
    track: Track?
) : ViewModel() {
    private val statePlayerLiveData = MutableLiveData(trackAnalysis(track))

    init {
        track?.let {
            setDataSource(it.previewUrl)
            preparePlayer()
            setOnPreparedListener {
                statePlayerLiveData.postValue(PlayerState.STATE_PREPARED)
            }
            setOnCompletionListener {
                statePlayerLiveData.postValue(PlayerState.STATE_COMPLETED)
            }
        } ?: run {
            statePlayerLiveData.postValue(PlayerState.STATE_ERROR)
        }
    }

    // Анализ трека
    private fun trackAnalysis(track: Track?): PlayerState {
        return if (track == null) {
            PlayerState.STATE_ERROR
        } else {
            if (!track.collectionName!!.isNotEmpty()) {
                PlayerState.STATE_DEFAULT
            } else {
                PlayerState.STATE_NO_ALBUM_NAME
            }
        }
    }

    // Получение состояния плеера
    fun getStatePlayerLiveData(): LiveData<PlayerState> = statePlayerLiveData

    // Изменение состояния плеера
    fun updateStatePlayerLiveData(state: PlayerState) {
        if (statePlayerLiveData.value != state) {
            statePlayerLiveData.postValue(state)
        }
    }

    // Изменение состояния плеера после клика по кнопке Play
     fun changeStatePlayerAfterClick() {
        when (statePlayerLiveData.value) {
            PlayerState.STATE_PLAYING -> statePlayerLiveData.postValue(PlayerState.STATE_PAUSED)
            PlayerState.STATE_PAUSED, PlayerState.STATE_PREPARED -> statePlayerLiveData.postValue(PlayerState.STATE_PLAYING)
            else -> {}
        }
    }

    // Плеер
    private fun setDataSource(url: String?) {
        audioPlayerInteractor.setDataSource(url)
    }

    private fun preparePlayer() {
        audioPlayerInteractor.preparePlayer()
    }

    fun start() {
        audioPlayerInteractor.start()
    }

    fun pause() {
        audioPlayerInteractor.pause()
    }

    fun currentPosition(): String {
        return audioPlayerInteractor.currentPosition()
    }

    private fun setOnPreparedListener(listener: MediaPlayer.OnPreparedListener) {
        audioPlayerInteractor.setOnPreparedListener(listener)
    }

    private fun setOnCompletionListener(listener: MediaPlayer.OnCompletionListener) {
        audioPlayerInteractor.setOnCompletionListener(listener)
    }

    private fun release() {
        audioPlayerInteractor.release()
    }

    override fun onCleared() {
        super.onCleared()
        release()

    }
}

enum class PlayerState {STATE_PLAYING,
    STATE_PAUSED,
    STATE_PREPARED,
    STATE_ERROR,
    STATE_COMPLETED,
    STATE_DEFAULT,
    STATE_NO_ALBUM_NAME

}











