package com.iesam.rememora.app.domain

sealed class ErrorApp {
    object UnknownError: ErrorApp()
    object NetworkError: ErrorApp()
}
