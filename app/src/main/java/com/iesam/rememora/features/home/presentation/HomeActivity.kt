package com.iesam.rememora.features.home.presentation

import android.content.Context
import android.media.AudioManager
import android.os.Bundle
import android.widget.Button
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import com.iesam.rememora.R
import com.iesam.rememora.databinding.ActivityHomeBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {

    private var _binding: ActivityHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<HomeViewModel>()

    private lateinit var audioManager: AudioManager
    private lateinit var volumeDownBottom: Button
    private lateinit var volumeUpBottom: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        audioManager = getSystemService(Context.AUDIO_SERVICE) as AudioManager
        volumeDownBottom = findViewById(R.id.volume_down)
        volumeUpBottom = findViewById(R.id.volume_up)

        setupView()
    }

    private fun setupView() {
        binding.apply {
            volumeDownBottom.setOnClickListener {
                volumeDown()
            }
            volumeUpBottom.setOnClickListener {
                volumeUp()
            }
            actionImage.setOnClickListener {
                Navigation.findNavController(this@HomeActivity, R.id.fragment_container)
                    .navigate(R.id.fragment_imagen)
            }
            actionVideo.setOnClickListener {
                Navigation.findNavController(this@HomeActivity, R.id.fragment_container)
                    .navigate(R.id.fragment_video)
            }
            actionMusica.setOnClickListener {
                Navigation.findNavController(this@HomeActivity, R.id.fragment_container)
                    .navigate(R.id.fragment_music)
            }
            actionAudio.setOnClickListener {
                Navigation.findNavController(this@HomeActivity, R.id.fragment_container)
                    .navigate(R.id.fragment_audio)
            }
            actionAccount.setOnClickListener {
                Navigation.findNavController(this@HomeActivity, R.id.fragment_container)
                    .navigate(R.id.fragment_logout)
            }
        }
    }

    private fun setupObserver() {
        val observer = Observer<HomeViewModel.UiState> {
            if (it.isLoading) {
            } else {
            }
            it.errorApp?.let {
            }
            if (it.account == null) {
                Navigation.findNavController(this, R.id.fragment_container)
                    .navigate(R.id.fragment_login)
            } else {

            }
        }
        viewModel.uiState.observe(this, observer)
    }

    private fun volumeDown() {
        val volume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)
        val newVolume = maxOf(volume - 1, 0)
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, newVolume, 0)
    }

    private fun volumeUp() {
        val volume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)
        val maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)
        val newVolume = minOf(volume + 1, maxVolume)
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, newVolume, 0)
    }
}