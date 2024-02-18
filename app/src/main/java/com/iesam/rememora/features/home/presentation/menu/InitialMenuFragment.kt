package com.iesam.rememora.features.home.presentation.menu

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.getkeepsafe.taptargetview.TapTargetView
import com.iesam.rememora.R
import com.iesam.rememora.app.extensions.createTarget
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
        tutorial()
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

    private fun tutorial() {
        val targetPhoto = binding.photoSection.createTarget(
            getString(R.string.tutorial_title_photo),
            getString(R.string.tutorial_description_photo), 130
        )
        val targetVideo = binding.videoSection.createTarget(
            getString(R.string.tutorial_title_video),
            getString(R.string.tutorial_description_video), 140
        )
        val targetMusic = binding.musicSection.createTarget(
            getString(R.string.tutorial_title_music),
            getString(R.string.tutorial_description_music), 140
        )
        val targetAudio = binding.audioSection.createTarget(
            getString(R.string.tutorial_title_audio),
            getString(R.string.tutorial_description_audio), 140
        )

        TapTargetView.showFor(activity, targetPhoto, object : TapTargetView.Listener() { //fotos
                override fun onTargetClick(view: TapTargetView) {
                    super.onTargetClick(view)
                    TapTargetView.showFor(activity, targetVideo, object : TapTargetView.Listener() { //videos
                        override fun onTargetClick(view: TapTargetView) {
                            super.onTargetClick(view)
                            TapTargetView.showFor(activity, targetMusic, object : TapTargetView.Listener() { //musica
                                    override fun onTargetClick(view: TapTargetView) {
                                        super.onTargetClick(view)
                                        TapTargetView.showFor(activity, targetAudio) //audio
                                    }
                                }
                            )
                        }
                    })
                }
            }
        )
    }

}