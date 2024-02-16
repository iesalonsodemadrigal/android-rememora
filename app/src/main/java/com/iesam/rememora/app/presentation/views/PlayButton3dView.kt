package com.iesam.rememora.app.presentation.views

import android.content.Context
import android.util.AttributeSet
import androidx.core.content.ContextCompat
import com.iesam.rememora.R

class PlayButton3dView(context: Context, attrs: AttributeSet? = null) :
    Button3dView(context, attrs) {

    init {
        background = ContextCompat.getDrawable(this.context, R.drawable.state_3d_button_home)
    }
}