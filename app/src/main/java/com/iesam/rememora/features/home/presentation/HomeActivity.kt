package com.iesam.rememora.features.home.presentation

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.Navigation
import com.getkeepsafe.taptargetview.TapTargetView
import com.iesam.rememora.R
import com.iesam.rememora.app.extensions.createTarget
import com.iesam.rememora.databinding.ActivityHomeBinding
import dagger.hilt.android.AndroidEntryPoint
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment

@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {

    private var _binding: ActivityHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<HomeViewModel>()
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupNavigation()
        val intent = intent
        if (intent != null){
            handleIntent(intent)
        }
        setupView()
    }

    private fun setupNavigation() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragment_container) as NavHostFragment
        navController = navHostFragment.navController
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        handleIntent(intent)
    }

    override fun onResume() {
        super.onResume()
        val intent = intent

        if (intent != null){
            handleIntent(intent)
        }
    }

    private fun handleIntent(intent: Intent?) {
        val uri: Uri? = intent?.data
        val action: String? = intent?.action

        if (uri.toString() == "rememora://images" && action == "android.intent.action.VIEW") {
            navController.handleDeepLink(intent)
        }
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
        //tutorial()
    }

    private fun tutorial() {
        val targetPhoto = binding.actionImage.createTarget(
            getString(R.string.tutorial_title_photo),
            getString(R.string.tutorial_description_photo), 100
        )

        TapTargetView.showFor(this, targetPhoto)
    }
}