package com.iesam.rememora.features.audio.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iesam.rememora.app.domain.ErrorApp
import com.iesam.rememora.app.presentation.error.ErrorUiModel
import com.iesam.rememora.app.presentation.error.toErrorUiModel
import com.iesam.rememora.features.audio.domain.Audio
import com.iesam.rememora.features.audio.domain.GetAudiosUseCase
import com.iesam.rememora.ia.domain.GetIntentionIAUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AudioPlayerViewModel @Inject constructor(
    private val useCase: GetAudiosUseCase,
    private val getIntentionIAUseCase: GetIntentionIAUseCase
) :
    ViewModel() {

    private val _uiState = MutableLiveData<UiState>()
    val uiState: LiveData<UiState> get() = _uiState

    fun getListAudios() {
        _uiState.value = UiState(isLoading = true)
        viewModelScope.launch(Dispatchers.IO) {
            useCase().fold(
                { responseError(it) },
                { responseSucess(it) }
            )
        }
    }

    private fun responseError(error: ErrorApp) {
        _uiState.postValue(UiState(errorApp = error.toErrorUiModel {
            getListAudios()
        }))
    }

    private fun responseSucess(listAudios: List<Audio>) {
        _uiState.postValue(UiState(audios = listAudios))
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
        val audios: List<Audio>? = null,
        val intention: String? = null,
    )
}