package com.iesam.rememora.features.home.presentation

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.speech.RecognizerIntent
import android.speech.tts.TextToSpeech
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.google.android.material.snackbar.Snackbar
import com.iesam.rememora.R
import com.iesam.rememora.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var textToSpeech: TextToSpeech

    private val resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                val data: Intent? = it.data
                if (data != null) {
                    val result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
                    val spokenText = result?.get(0) ?: ""
                    when (spokenText) {
                        getString(R.string.command_photos) ->
                            Navigation.findNavController(
                                requireActivity(),
                                R.id.fragment_container
                            )
                                .navigate(R.id.fragment_imagen)

                        getString(R.string.command_video) ->
                            Navigation.findNavController(
                                requireActivity(),
                                R.id.fragment_container
                            )
                                .navigate(R.id.fragment_video)

                        getString(R.string.command_music) ->
                            Navigation.findNavController(
                                requireActivity(),
                                R.id.fragment_container
                            )
                                .navigate(R.id.fragment_music)

                        getString(R.string.command_audio) ->
                            Navigation.findNavController(
                                requireActivity(),
                                R.id.fragment_container
                            )
                                .navigate(R.id.fragment_audio)

                        else -> speakOut(getString(R.string.voice_response_command_not_exist))

                    }

                }
            }
        }

    private fun speakOut(text: String) {
        textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null, "")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        setupView()
        return binding.root
    }

    private fun setupView() {
        binding.microButton.setOnClickListener {
            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.RECORD_AUDIO
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                promptSpeechInput()
            } else {
                Snackbar.make(
                    binding.root,
                    getString(R.string.no_voice_permissions),
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun promptSpeechInput() {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        intent.putExtra(
            RecognizerIntent.EXTRA_LANGUAGE_MODEL,
            RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
        )
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, getString(R.string.language))
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, getString(R.string.extra_prompt_recognizer))

        resultLauncher.launch(intent)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        textToSpeech = TextToSpeech(requireContext()) { status ->
            if (status == TextToSpeech.SUCCESS) {
                Locale(getString(R.string.language), getString(R.string.country))
            }
        }
    }

}