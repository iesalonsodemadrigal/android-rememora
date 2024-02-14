package com.iesam.rememora.features.home.presentation.menu

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
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

    private var brightnessValue = 100

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
                val context = requireContext()
                val settingsCanWrite = hasWriteSettingsPermission(context)
                if (!settingsCanWrite) {
                    changeWriteSettingsPermission(context)
                } else {
                    if (brightnessValue <= 245) {
                        brightnessValue += 10
                        changeScreenBrightness(context, brightnessValue)
                        val k = brightnessValue.toDouble() / 255
                    }
                }
            }
            actionBrightnessDown.setOnClickListener {
                val context = requireContext()
                val settingsCanWrite = hasWriteSettingsPermission(context)
                if (!settingsCanWrite) {
                    changeWriteSettingsPermission(context)
                } else {
                    if (brightnessValue >= 0) {
                        brightnessValue -= 10
                        changeScreenBrightness(context, brightnessValue)
                        val k = brightnessValue.toDouble() / 255
                    }
                }
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

    private fun hasWriteSettingsPermission(context: Context): Boolean {
        return Settings.System.canWrite(context)
    }

    private fun changeWriteSettingsPermission(context: Context) {
        val intent = Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS)
        startActivity(intent)
    }

    private fun changeScreenBrightness(context: Context, screenBrightnessValue: Int) {
        Settings.System.putInt(
            context.contentResolver,
            Settings.System.SCREEN_BRIGHTNESS_MODE,
            Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL
        )
        Settings.System.putInt(
            context.contentResolver,
            Settings.System.SCREEN_BRIGHTNESS, screenBrightnessValue
        )
    }
}