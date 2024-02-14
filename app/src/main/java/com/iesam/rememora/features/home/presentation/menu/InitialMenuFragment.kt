package com.iesam.rememora.features.home.presentation.menu

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.iesam.rememora.databinding.FragmentInitialMenuBinding
import com.iesam.rememora.features.home.presentation.HomeActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class InitialMenuFragment : Fragment() {
    private var _binding: FragmentInitialMenuBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentInitialMenuBinding.inflate(inflater, container, false)
        setupView()
        return binding.root
    }

    private fun setupView() {
        binding.apply {
            (requireActivity() as HomeActivity).hideHomeButton()
            actionBrightnessUp.setOnClickListener {
                val activity = requireActivity()
                val window = activity.window
                val layoutParams = window.attributes
                layoutParams.screenBrightness = layoutParams.screenBrightness + 20.1f
                window.attributes = layoutParams
            }
            actionBrightnessDown.setOnClickListener {
                val activity = requireActivity()
                val window = activity.window
                val layoutParams = window.attributes
                layoutParams.screenBrightness = layoutParams.screenBrightness - 20.1f
                window.attributes = layoutParams
            }
            actionAudio.setOnClickListener {
                findNavController().navigate(
                    InitialMenuFragmentDirections.actionGlobalAudioFragment()
                )
            }
            actionVideo.setOnClickListener {
                findNavController().navigate(
                    InitialMenuFragmentDirections.actionGlobalVideoFragment()
                )
            }
            actionPhoto.setOnClickListener {
                findNavController().navigate(
                    InitialMenuFragmentDirections.actionGlobalImageFragment()
                )
            }
            actionMusic.setOnClickListener {
                findNavController().navigate(
                    InitialMenuFragmentDirections.actionGlobalMusicFragment()
                )
            }
        }
    }

}