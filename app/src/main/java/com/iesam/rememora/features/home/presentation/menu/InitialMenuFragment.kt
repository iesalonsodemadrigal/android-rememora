package com.iesam.rememora.features.home.presentation.menu

import android.graphics.Typeface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.getkeepsafe.taptargetview.TapTarget
import com.getkeepsafe.taptargetview.TapTargetView
import com.iesam.rememora.R
import com.iesam.rememora.app.extensions.createTarget
import com.iesam.rememora.app.extensions.show
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
        val targetPhoto = binding.photoSection.createTarget("PHOTO", "")
        val targetVideo = binding.videoSection.createTarget("VIDEO", "")
        val targetMusic = binding.musicSection.createTarget("MUSICA", "")
        val targetAudio = binding.audioSection.createTarget("AUDIO", "")

        TapTargetView.showFor(activity, targetPhoto,
            object : TapTargetView.Listener() {
                override fun onTargetClick(view: TapTargetView) {
                    super.onTargetClick(view)
                    TapTargetView.showFor(activity,  targetVideo, object : TapTargetView.Listener() {
                        override fun onTargetClick(view: TapTargetView) {
                            super.onTargetClick(view)
                            TapTargetView.showFor(activity,  targetMusic, object : TapTargetView.Listener() {
                                override fun onTargetClick(view: TapTargetView) {
                                    super.onTargetClick(view)
                                    TapTargetView.showFor(activity, targetAudio )
                                }
                            })
                        }
                    })
                }
            })


        binding.apply {
            (requireActivity() as HomeActivity).hideHomeButton()
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