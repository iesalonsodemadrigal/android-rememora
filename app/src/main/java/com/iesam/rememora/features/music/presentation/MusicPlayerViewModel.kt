package com.iesam.rememora.features.music.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iesam.rememora.app.domain.ErrorApp
import com.iesam.rememora.app.presentation.error.ErrorUiModel
import com.iesam.rememora.app.presentation.error.toErrorUiModel
import com.iesam.rememora.features.audio.presentation.AudioPlayerViewModel
import com.iesam.rememora.features.music.domain.GetMusicListUseCase
import com.iesam.rememora.features.music.domain.Song
import com.iesam.rememora.ia.domain.GetIntentionIAUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MusicPlayerViewModel @Inject constructor(
    private val getMusicListUseCase: GetMusicListUseCase,
    private val getIntentionIAUseCase: GetIntentionIAUseCase
) :
    ViewModel() {
    private val _uiState = MutableLiveData<UiState>()
    val uiState: LiveData<UiState> = _uiState
    fun loadMusicList() {
        viewModelScope.launch(Dispatchers.IO) {
            getMusicListUseCase.invoke().fold(
                { responseError(it) }, { responseSuccess(it) }
            )
        }
    }

    private fun responseError(it: ErrorApp) {
        _uiState.postValue(UiState(errorApp = it.toErrorUiModel {
            loadMusicList()
        }))
    }

    private fun responseSuccess(it: List<Song>) {
        _uiState.postValue(UiState(musicList = it))
    }

    fun getIntention(phrase: String) {
        _uiState.value = UiState(isLoading = true)
        viewModelScope.launch(Dispatchers.IO) {
            getIntentionIAUseCase(phrase).fold({
                responseErrorIA(it)
            }, {
                responseSuccessIA(it)
            })
        }
    }

    private fun responseErrorIA(error: ErrorApp) {
        _uiState.postValue(UiState(errorApp = error.toErrorUiModel()))
    }

    private fun responseSuccessIA(intention: String) {
        _uiState.postValue(UiState(intention = intention))
    }


    data class UiState(
        val isLoading: Boolean = false,
        val musicList: List<Song>? = null,
        val errorApp: ErrorUiModel? = null,
        val intention: String? = null,
    )
}