package com.iesam.rememora.features.music.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iesam.rememora.app.domain.ErrorApp
import com.iesam.rememora.app.presentation.error.ErrorUiModel
import com.iesam.rememora.app.presentation.error.toErrorUiModel
import com.iesam.rememora.features.music.domain.GetMusicListUseCase
import com.iesam.rememora.features.music.domain.Music
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MusicPlayerViewModel @Inject constructor(private val getMusicListUseCase: GetMusicListUseCase) :
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

    private fun responseSuccess(it: List<Music>) {
        _uiState.postValue(UiState(musicList = it))
    }

    data class UiState(
        val musicList: List<Music>? = null,
        val errorApp: ErrorUiModel? = null
    )
}