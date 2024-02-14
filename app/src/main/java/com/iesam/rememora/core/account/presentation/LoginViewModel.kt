package com.iesam.rememora.core.account.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iesam.rememora.app.domain.ErrorApp
import com.iesam.rememora.app.presentation.error.ErrorUiModel
import com.iesam.rememora.app.presentation.error.toErrorUiModel
import com.iesam.rememora.core.account.domain.Account
import com.iesam.rememora.core.account.domain.CheckAccountLiveUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val checkAccountLiveUseCase: CheckAccountLiveUseCase) :
    ViewModel() {

    private val _uiState = MutableLiveData<UiState>()
    val uiState: LiveData<UiState> get() = _uiState

    fun checkSession() {
        _uiState.value = UiState(isLoading = true)
        viewModelScope.launch(Dispatchers.IO) {
            checkAccountLiveUseCase().fold(
                { responseErrorCheckSession(it) },
                { responseSuccessCheckSession(it) }
            )
        }
    }

    private fun responseErrorCheckSession(errorApp: ErrorApp) {
        _uiState.postValue(UiState(errorApp = errorApp.toErrorUiModel { checkSession() }))
    }

    private fun responseSuccessCheckSession(account: Account?) {
        _uiState.postValue(UiState(hasSession = account != null))
    }

    data class UiState(
        val errorApp: ErrorUiModel? = null,
        val isLoading: Boolean = false,
        val hasSession: Boolean = false
    )
}