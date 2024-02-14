package com.iesam.rememora.app.presentation.error

import com.iesam.rememora.app.domain.ErrorApp

class ErrorUiModelFactory {
    fun create(error: ErrorApp, onClick: (() -> Unit)? = null): ErrorUiModel {
        return when (error) {
            ErrorApp.UnknownError -> UnknownErrorUiModel(onClick)
            ErrorApp.DataError -> DataErrorUiModel(onClick)
            ErrorApp.InternetError -> InternetErrorUiModel(onClick)
            ErrorApp.SessionError -> SessionErrorUiModel(onClick)
            ErrorApp.ServerError -> ServerErrorUiModel(onClick)
        }
    }
}