package com.iesam.rememora.features.images.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iesam.rememora.app.domain.ErrorApp
import com.iesam.rememora.app.presentation.error.ErrorUiModel
import com.iesam.rememora.app.presentation.error.toErrorUiModel
import com.iesam.rememora.core.emotion.domain.CheckFaceEmotionUseCase
import com.iesam.rememora.core.emotion.domain.CheckFaceEmotionUseCase
import com.iesam.rememora.features.images.domain.GetImagesUseCase
import com.iesam.rememora.features.images.domain.Image
import com.iesam.rememora.features.images.domain.SaveImageUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ImagePlayerViewModel @Inject constructor(
    private val getImagesUseCase: GetImagesUseCase,
    private val saveImageUseCase: SaveImageUseCase,
    private val checkFaceEmotionUseCase: CheckFaceEmotionUseCase
) :
    ViewModel() {
    private val _uiState = MutableLiveData<UiState>()
    val uiState: LiveData<UiState> get() = _uiState

    fun saveImage(image: Image, emotion: Int) {
        _uiState.value = UiState(isLoading = true)
        viewModelScope.launch(Dispatchers.IO) {
            saveImageUseCase(
                image,
                emotion
            )
        }

    }

    fun getImages() {
        _uiState.value = UiState(isLoading = true)
        viewModelScope.launch(Dispatchers.IO) {
            getImagesUseCase().fold({
                responseError(it)
            }, {
                responseSuccess(it)
            })
        }
    }

    fun setFaceEmotion(
        smilingProbability: Float,
        leftEyeOpenProbability: Float,
        rightEyeOpenProbability: Float
    ) {
        viewModelScope.launch {
            checkFaceEmotionUseCase(
                CheckFaceEmotionUseCase.Input(
                    smilingProbability,
                    leftEyeOpenProbability,
                    rightEyeOpenProbability
                )
            ).fold({

            }, {
                _uiState.postValue(UiState(emotion = it))
            })
        }
    }

    private fun responseError(error: ErrorApp) {
        _uiState.postValue(UiState(errorApp = error.toErrorUiModel {
            getImages()
        }))
    }

    private fun responseSuccess(images: List<Image>) {
        _uiState.postValue(UiState(images = images))
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
        val images: List<Image>? = null,
        val intention: String? = null,
        var emotion: Int? = null
    )
}