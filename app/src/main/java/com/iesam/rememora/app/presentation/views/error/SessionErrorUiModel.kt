package com.iesam.rememora.app.presentation.views.error

import com.iesam.rememora.R

class SessionErrorUiModel(private val onClick: (() -> Unit)? = null) : ErrorUiModel {
    override fun getImage(): Int = R.drawable.image_error

    override fun getTitle(): Int = R.string.title_unknown_error

    override fun getDescription(): Int = R.string.description_unknown_error

    override fun onClickRetry(): (() -> Unit)? = onClick
}