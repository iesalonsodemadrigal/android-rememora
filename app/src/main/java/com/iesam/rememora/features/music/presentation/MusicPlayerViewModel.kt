package com.iesam.rememora.features.music.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iesam.rememora.app.domain.ErrorApp
import com.iesam.rememora.features.music.domain.GetMusicListUseCase
import com.iesam.rememora.features.music.domain.Music
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MusicPlayerViewModel(private val getMusicListUseCase: GetMusicListUseCase) : ViewModel() {
    private val _uiState = MutableLiveData<UiState>()
    val uiState: LiveData<UiState> = _uiState

    data class UiState(
        val musicList: List<Music>? = null,
        val errorApp: ErrorApp? = null
    )

    fun loadMusicList() {
        viewModelScope.launch(Dispatchers.IO) {
            getMusicListUseCase.invoke().fold(
                { responseError(it) }, { responseSucces(it) }
            )
        }
    }

    private fun responseError(it: ErrorApp) {
        _uiState.postValue(UiState(errorApp = it))
    }

    private fun responseSucces(it: List<Music>) {
        _uiState.postValue(UiState(musicList = it))
    }
}