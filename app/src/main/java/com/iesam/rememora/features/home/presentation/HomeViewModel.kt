package com.iesam.rememora.features.home.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iesam.rememora.app.domain.ErrorApp
import com.iesam.rememora.core.emotion.domain.CheckFaceEmotionUseCase
import com.iesam.rememora.ia.domain.GetIntentionIAUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getIntentionIAUseCase: GetIntentionIAUseCase,
    private val checkFaceEmotionUseCase: CheckFaceEmotionUseCase
) :
    ViewModel() {

    private val _uiState = MutableLiveData<UiState>()
    val uiState: LiveData<UiState> get() = _uiState

    fun getIntention(prompt: String) {
        _uiState.value = UiState(isLoading = true)
        viewModelScope.launch(Dispatchers.IO) {
            getIntentionIAUseCase(prompt).fold({
                responseErrorIA(it)
            }, {
                responseSuccessIA(it)
            })
        }
    }

    private fun responseErrorIA(error: ErrorApp) {
        _uiState.postValue(UiState(error))
    }

    private fun responseSuccessIA(intention: String) {
        _uiState.postValue(UiState(intention = intention))
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

            })
        }
    }


    data class UiState(
        val errorApp: ErrorApp? = null,
        val isLoading: Boolean = false,
        val intention: String? = null,
        val emotion: String? = null
    )


}