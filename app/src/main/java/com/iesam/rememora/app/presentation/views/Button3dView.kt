package com.iesam.rememora.app.presentation.views

import android.content.Context
import android.media.AudioManager
import android.media.ToneGenerator
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.AttributeSet
import androidx.core.content.ContextCompat
import com.iesam.rememora.R


open class Button3dView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : androidx.appcompat.widget.AppCompatImageButton(context, attrs) {

    init {
        background = ContextCompat.getDrawable(this.context, R.drawable.state_3d_button)
    }

    override fun performClick(): Boolean {
        //isSelected = !isSelected
        runVibration()
        //playBeep()
        return super.performClick()
    }

    private fun playBeep() {
        val toneG = ToneGenerator(AudioManager.STREAM_ALARM, 100)
        toneG.startTone(ToneGenerator.TONE_CDMA_ALERT_CALL_GUARD, 200)
    }

    private fun runVibration() {
        val vibrator =
            ContextCompat.getSystemService(this.context, Vibrator::class.java) as Vibrator
        vibrator.vibrate(VibrationEffect.createOneShot(20, VibrationEffect.DEFAULT_AMPLITUDE))
    }
}