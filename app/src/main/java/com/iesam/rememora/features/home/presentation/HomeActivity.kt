package com.iesam.rememora.features.home.presentation

import android.content.Context
import android.media.AudioManager
import android.os.Bundle
import android.widget.Button
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.Navigation
import com.iesam.rememora.R
import com.iesam.rememora.app.extensions.hide
import com.iesam.rememora.app.extensions.show
import com.iesam.rememora.databinding.ActivityHomeBinding
import com.iesam.rememora.features.home.presentation.menu.InitialMenuFragmentDirections
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {

    private var _binding: ActivityHomeBinding? = null
    private val binding get() = _binding!!


    private lateinit var audioManager: AudioManager
    private lateinit var volumeDownBottom: Button
    private lateinit var volumeUpBottom: Button

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
    }

    fun hideHomeButton() {
        binding.actionBackToHome.hide()
    }

    fun showHomeButton() {
        binding.actionBackToHome.show()
    }
}