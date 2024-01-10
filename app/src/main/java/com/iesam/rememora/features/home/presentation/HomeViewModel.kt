package com.iesam.rememora.features.home.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iesam.rememora.app.domain.ErrorApp
import com.iesam.rememora.core.account.domain.Account
import com.iesam.rememora.features.home.domain.IsAccountUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val isAccountUseCase: IsAccountUseCase) :
    ViewModel() {
    private val _uiState = MutableLiveData<UiState>()
    val uiState: LiveData<UiState> get() = _uiState

    fun checkAccount() {
        _uiState.value = UiState(isLoading = true)
        viewModelScope.launch(Dispatchers.IO) {
            isAccountUseCase().fold(
                { responseError(it) },
                { responseSuccess(it) }
            )
        }
    }

    private fun responseError(error: ErrorApp) {
        _uiState.postValue(UiState(errorApp = error))
    }

    private fun responseSuccess(account: Account?) {
        _uiState.postValue(UiState(account = account))
    }

    data class UiState(
        val errorApp: ErrorApp? = null,
        val isLoading: Boolean = false,
        val account: Account? = null
    )
}