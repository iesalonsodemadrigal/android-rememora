package com.iesam.rememora.features.video.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iesam.rememora.app.domain.ErrorApp
import com.iesam.rememora.app.presentation.views.error.ErrorUiModel
import com.iesam.rememora.app.presentation.views.error.toErrorUiModel
import com.iesam.rememora.features.video.domain.GetVideosUseCase
import com.iesam.rememora.features.video.domain.Video
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VideoPlayerViewModel @Inject constructor(private val getVideosUseCase: GetVideosUseCase) :
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

    data class UiState(
        val errorApp: ErrorUiModel? = null,
        val isLoading: Boolean = false,
        val videos: List<Video>? = null
    )
}