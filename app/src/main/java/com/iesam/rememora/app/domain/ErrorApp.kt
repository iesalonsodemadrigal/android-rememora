package com.iesam.rememora.app.domain

sealed class ErrorApp {
    object UnknownError : ErrorApp()
    object DataError : ErrorApp()
    object InternetError : ErrorApp()
    object SessionError : ErrorApp()
    object ServerError : ErrorApp()
}
