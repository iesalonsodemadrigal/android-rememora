package com.iesam.rememora.core.account.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iesam.rememora.app.domain.ErrorApp
import com.iesam.rememora.core.account.domain.DeleteAccountUseCase
import com.iesam.rememora.core.account.domain.LogoutUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LogoutViewModel @Inject constructor(
    private val logoutUseCase: LogoutUseCase,
    private val deleteAccountUseCase: DeleteAccountUseCase
) : ViewModel() {
    private val _uiState = MutableLiveData<UiState>()
    val uiState: LiveData<UiState> get() = _uiState

    fun logout() {
        _uiState.value = UiState(isLoading = true)
        viewModelScope.launch(Dispatchers.IO) {
            logoutUseCase().fold(
                { responseError(it) },
                { responseSuccess() })
        }
    }

    fun deleteAccount() {
        _uiState.value = UiState(isLoading = true)
        viewModelScope.launch(Dispatchers.IO) {
            deleteAccountUseCase().fold(
                { responseError(it) },
                { responseSuccess() })
        }
    }

    private fun responseError(error: ErrorApp) {
        _uiState.postValue(UiState(errorApp = error))
    }

    private fun responseSuccess() {
        _uiState.postValue(UiState(isOk = true))
    }

    data class UiState(
        val errorApp: ErrorApp? = null,
        val isLoading: Boolean = false,
        val isOk: Boolean = false
    )
}