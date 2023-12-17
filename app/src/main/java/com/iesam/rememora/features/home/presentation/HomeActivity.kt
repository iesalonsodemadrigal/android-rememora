package com.iesam.rememora.features.home.presentation

import android.os.Bundle
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
                if (it.errorApp != null) {
                } else {
                    if (!it.account) {
                        Navigation.findNavController(this@HomeActivity, R.id.fragment_container)
                            .navigate(R.id.fragment_login)
                    }
                }
            }
        }
        viewModel.uiState.observe(this, observer)
    }

    override fun onResume() {
        super.onResume()
        viewModel.isAccount()
    }
}