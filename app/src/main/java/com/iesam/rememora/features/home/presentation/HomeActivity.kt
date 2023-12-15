package com.iesam.rememora.features.home.presentation

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.Navigation
import com.google.firebase.auth.FirebaseAuth
import com.iesam.rememora.R
import com.iesam.rememora.databinding.ActivityHomeBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {

    private var _binding: ActivityHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupView()
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

    private val auth by lazy {
        FirebaseAuth.getInstance()
    }

    override fun onResume() {
        super.onResume()
        if (!isAuthClient()) {
            binding.layoutMenu.visibility = View.GONE
            binding.actionAccount.visibility = View.GONE
            Navigation.findNavController(this@HomeActivity, R.id.fragment_container)
                .navigate(R.id.fragment_login)
        }
    }

    private fun isAuthClient(): Boolean {
        return auth.currentUser != null
    }
}