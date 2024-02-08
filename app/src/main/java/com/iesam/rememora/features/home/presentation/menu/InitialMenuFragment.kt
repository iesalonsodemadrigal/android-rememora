package com.iesam.rememora.features.home.presentation.menu

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.iesam.rememora.databinding.FragmentInitialMenuBinding
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
            audioSection.setOnClickListener {
                findNavController().navigate(
                    InitialMenuFragmentDirections.actionGlobalAudioFragment()
                )
            }
            videoSection.setOnClickListener {
                findNavController().navigate(
                    InitialMenuFragmentDirections.actionGlobalVideoFragment()
                )
            }
            imageSection.setOnClickListener {
                findNavController().navigate(
                    InitialMenuFragmentDirections.actionGlobalImageFragment()
                )
            }
            musicSection.setOnClickListener {
                findNavController().navigate(
                    InitialMenuFragmentDirections.actionGlobalMusicFragment()
                )
            }
        }
    }

}