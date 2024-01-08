package com.iesam.rememora.app.presentation.views.error

import com.iesam.rememora.app.domain.ErrorApp

fun ErrorApp.toErrorUiModel(onClick: (() -> Unit)? = null): ErrorUiModel {
    return ErrorUiModelFactory().create(this, onClick)
}