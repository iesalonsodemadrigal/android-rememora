package com.iesam.rememora.features.home.presentation

import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupView()
        setupObserver()
    }

    private fun setupView() {
        binding.apply {
            actionImage.setOnClickListener {
                vibration()
                Navigation.findNavController(this@HomeActivity, R.id.fragment_container)
                    .navigate(R.id.fragment_imagen)
            }
            actionVideo.setOnClickListener {
                vibration()
                Navigation.findNavController(this@HomeActivity, R.id.fragment_container)
                    .navigate(R.id.fragment_video)
            }
            actionMusica.setOnClickListener {
                vibration()
                Navigation.findNavController(this@HomeActivity, R.id.fragment_container)
                    .navigate(R.id.fragment_music)

            }
            actionAudio.setOnClickListener {
                vibration()
                Navigation.findNavController(this@HomeActivity, R.id.fragment_container)
                    .navigate(R.id.fragment_audio)
            }
            actionAccount.setOnClickListener {
                vibration()
                Navigation.findNavController(this@HomeActivity, R.id.fragment_container)
                    .navigate(R.id.fragment_logout)
            }
        }
    }

    private fun vibration() {
        val vibrator = ContextCompat.getSystemService(this, Vibrator::class.java) as Vibrator

        vibrator.vibrate(VibrationEffect.createOneShot(20, VibrationEffect.DEFAULT_AMPLITUDE))
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
}