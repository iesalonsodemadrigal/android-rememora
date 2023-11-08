package com.iesam.rememora.features.home.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.findNavController
import com.iesam.rememora.R
import com.iesam.rememora.databinding.ActivityHomeBinding

class HomeActivity : AppCompatActivity() {

    private var _binding : ActivityHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupView()
    }
    private fun setupView(){
        binding.apply {
            actionImage.setOnClickListener{
                it.findNavController().navigate(R.id.fragment_imagen)
            }
            actionVideo.setOnClickListener {
                it.findNavController().navigate(R.id.fragment_video)
            }
            actionMusica.setOnClickListener {
                it.findNavController().navigate(R.id.fragment_music)
            }
            actionAudio.setOnClickListener {
                it.findNavController().navigate(R.id.action_audio)
            }
        }
    }
}