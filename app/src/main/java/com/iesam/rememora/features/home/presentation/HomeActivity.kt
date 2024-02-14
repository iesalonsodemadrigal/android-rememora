package com.iesam.rememora.features.home.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
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
        binding.labelBackToHome.hide()
    }

    fun showHomeButton() {
        binding.actionBackToHome.show()
        binding.labelBackToHome.show()
    }
}