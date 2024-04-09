package com.iesam.rememora.app.presentation.error

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.iesam.rememora.app.extensions.hide
import com.iesam.rememora.app.extensions.show
import com.iesam.rememora.databinding.ViewErrorBinding

class ErrorView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : FrameLayout(context, attrs) {
    private val binding = ViewErrorBinding.inflate(LayoutInflater.from(context), this, true)

    init {
        hide()
    }

    fun render(errorUiModel: ErrorUiModel) {
        show()
        binding.apply {
            imageError.setImageResource(errorUiModel.getImage())
            labelTitleError.setText(errorUiModel.getTitle())
            labelDescriptionError.setText(errorUiModel.getDescription())
            actionRetry.setOnClickListener {
                errorUiModel.onClickRetry()?.invoke()
            }
        }
    }

}