package com.iesam.rememora.features.video.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iesam.rememora.app.domain.ErrorApp
import com.iesam.rememora.app.presentation.error.ErrorUiModel
import com.iesam.rememora.app.presentation.error.toErrorUiModel
import com.iesam.rememora.features.audio.presentation.AudioPlayerViewModel
import com.iesam.rememora.features.video.domain.GetVideosUseCase
import com.iesam.rememora.features.video.domain.Video
import com.iesam.rememora.ia.domain.GetIntentionIAUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VideoPlayerViewModel @Inject constructor(
    private val getVideosUseCase: GetVideosUseCase,
    private val getIntentionIAUseCase: GetIntentionIAUseCase
) :
    ViewModel() {
    private val _uiState = MutableLiveData<UiState>()
    val uiState: LiveData<UiState> = _uiState

    fun getVideos() {
        _uiState.value = UiState(isLoading = true)
        viewModelScope.launch(Dispatchers.IO) {
            getVideosUseCase().fold(
                { responseError(it) },
                { responseSuccess(it) }
            )
        }
    }

    private fun responseError(error: ErrorApp) {
        _uiState.postValue(UiState(errorApp = error.toErrorUiModel {
            getVideos()
        }))
    }

    private fun responseSuccess(videos: List<Video>) {
        _uiState.postValue(UiState(videos = videos))
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
        val errorApp: ErrorUiModel? = null,
        val isLoading: Boolean = false,
        val videos: List<Video>? = null,
        val intention: String? = null
    )
}