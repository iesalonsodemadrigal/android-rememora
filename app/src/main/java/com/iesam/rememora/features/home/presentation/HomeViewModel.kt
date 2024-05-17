package com.iesam.rememora.features.home.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iesam.rememora.app.domain.ErrorApp
import com.iesam.rememora.core.emotion.domain.CheckFaceEmotionUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val checkFaceEmotionUseCase: CheckFaceEmotionUseCase) :
    ViewModel() {

    private val _uiState = MutableLiveData<UiState>()
    val uiState: LiveData<UiState> get() = _uiState

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

            })
        }
    }

    data class UiState(
        val errorApp: ErrorApp? = null,
        val isLoading: Boolean = false,
        val emotion: String? = null
    )


}