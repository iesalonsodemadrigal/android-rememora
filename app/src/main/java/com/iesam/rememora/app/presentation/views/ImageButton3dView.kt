package com.iesam.rememora.app.presentation.views

import android.content.Context
import android.media.AudioManager
import android.media.ToneGenerator
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.AttributeSet
import androidx.core.content.ContextCompat
import com.iesam.rememora.app.BUTTON_3D_SOUND_ACTIVE


open class ImageButton3dView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : androidx.appcompat.widget.AppCompatImageView(context, attrs) {

    override fun performClick(): Boolean {
        playBeep()
        runVibration()
        return super.performClick()
    }

    private fun playBeep() {
        if (BUTTON_3D_SOUND_ACTIVE) {
            val toneG = ToneGenerator(AudioManager.STREAM_ALARM, 100)
            toneG.startTone(ToneGenerator.TONE_CDMA_ALERT_CALL_GUARD, 200)
        }
    }

    private fun runVibration() {
        val vibrator =
            ContextCompat.getSystemService(this.context, Vibrator::class.java) as Vibrator
        vibrator.vibrate(VibrationEffect.createOneShot(50, VibrationEffect.DEFAULT_AMPLITUDE))
    }
}