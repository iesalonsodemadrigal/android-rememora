package com.iesam.rememora.features.home.presentation

import android.content.Context
import android.media.AudioManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import com.iesam.rememora.R
import com.iesam.rememora.app.extensions.hide
import com.iesam.rememora.app.extensions.show
import com.iesam.rememora.app.presentation.views.Button3dView
import com.iesam.rememora.databinding.ActivityHomeBinding
import com.iesam.rememora.databinding.ViewMediaplayerBinding
import com.iesam.rememora.features.home.presentation.menu.InitialMenuFragmentDirections
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {

    private var _binding: ActivityHomeBinding? = null
    private val binding get() = _binding!!
    private var _bindingMediaPlayer: ViewMediaplayerBinding? = null
    private val bindingMediaPlayer get() = _bindingMediaPlayer!!

    private lateinit var audioManager: AudioManager
    private lateinit var volumeDownBottom: Button3dView
    private lateinit var volumeUpBottom: Button3dView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.actionBackToHome.setOnClickListener {
            findNavController(R.id.fragment_container).navigate(
                InitialMenuFragmentDirections.actionToHome()
            )
        }
        hideHomeButton()
        _bindingMediaPlayer = ViewMediaplayerBinding.inflate(layoutInflater)
        audioManager = getSystemService(Context.AUDIO_SERVICE) as AudioManager
        volumeDownBottom = bindingMediaPlayer.volumeDown
        volumeUpBottom = bindingMediaPlayer.volumeUp
        setupView()
    }

    private fun setupView() {
        bindingMediaPlayer.apply {
            volumeUpBottom.setOnClickListener {
                volumeUp()
            }
            volumeDownBottom.setOnClickListener {
                volumeDown()
            }
        }
    }

    private fun volumeUp() {
        val volume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)
        val maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)
        val newVolume = minOf(volume + 1, maxVolume)
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, newVolume, 0)
    }

    private fun volumeDown() {
        val volume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)
        val newVolume = maxOf(volume - 1, 0)
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, newVolume, 0)
    }

    fun hideHomeButton() {
        binding.actionBackToHome.hide()
        binding.labelBackToHome.hide()
    }

    fun showHomeButton() {
        binding.actionBackToHome.show()
        binding.labelBackToHome.show()
    }
}