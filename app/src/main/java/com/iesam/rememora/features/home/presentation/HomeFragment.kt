package com.iesam.rememora.features.home.presentation

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.speech.tts.TextToSpeech
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
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

    private val viewModel by viewModels<HomeViewModel>()

    private lateinit var textToSpeech: TextToSpeech

    private lateinit var speechRecognizer: SpeechRecognizer

    private var destroySpeech = false

    private val resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                val data: Intent? = it.data
                if (data != null) {
                    val result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
                    val spokenText = result?.get(0) ?: ""
                    getIntention(spokenText)
                }
            }
        }

    private fun getIntention(phrase: String) {
        val prompt = getString(R.string.prompt_home, phrase)

        viewModel.getIntention(prompt)
    }

    private fun speakOut(text: String) {
        textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null, "")
    }

    private fun startListening() {
        if (destroySpeech) {
            createSpeechRecognizer()
            destroySpeech = false
        }
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        intent.putExtra(
            RecognizerIntent.EXTRA_LANGUAGE_MODEL,
            RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
        )
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, getString(R.string.language))
        speechRecognizer.startListening(intent)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        createSpeechRecognizer()
        setupView()
        return binding.root
    }

    private fun createSpeechRecognizer() {
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(requireContext())
        speechRecognizer.setRecognitionListener(object : RecognitionListener {
            override fun onReadyForSpeech(params: Bundle) {}
            override fun onBeginningOfSpeech() {}
            override fun onRmsChanged(rmsdB: Float) {}
            override fun onBufferReceived(buffer: ByteArray) {}
            override fun onEndOfSpeech() {}

            override fun onError(error: Int) {
                startListening()
            }

            override fun onResults(results: Bundle) {
                val matches = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                if (matches != null && matches.size > 0) {
                    val command = matches[0]
                    if (command.contains(getString(R.string.keyword_1)) || command.contains(
                            getString(R.string.keyword_2)
                        ) || command.contains(getString(R.string.keyword_3)) || command.contains(
                            getString(R.string.keyword_4)
                        )
                    ) {
                        getIntention(command)
                    } else {
                        startListening()
                    }
                }
            }

            override fun onPartialResults(partialResults: Bundle) {}
            override fun onEvent(eventType: Int, params: Bundle) {}
        })
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


    private fun setupView() {
        binding.microButton.setOnClickListener {
            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.RECORD_AUDIO
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                speechRecognizer.destroy()
                destroySpeech = true
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupObserver()

        textToSpeech = TextToSpeech(requireContext()) { status ->
            if (status == TextToSpeech.SUCCESS) {
                Locale(getString(R.string.language), getString(R.string.country))
            }
        }

        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.RECORD_AUDIO
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            startListening()
        } else {
            Snackbar.make(
                binding.root,
                getString(R.string.no_voice_permissions),
                Snackbar.LENGTH_SHORT
            ).show()
        }
    }

    private fun setupObserver() {
        val observer = Observer<HomeViewModel.UiState> {
            if (it.isLoading) {

            } else {
                if (it.errorApp != null) {

                } else {
                    it.intention?.apply {
                        handleResult(this.lowercase())
                    }
                }
            }
        }
        viewModel.uiState.observe(viewLifecycleOwner, observer)
    }

    private fun handleResult(intention: String) {
        if (intention.contains(getString(R.string.command_photos))) {
            speakOut(getString(R.string.voice_response_ok))
            Navigation.findNavController(
                requireActivity(),
                R.id.fragment_container
            )
                .navigate(R.id.fragment_imagen)
        } else if (intention.contains(getString(R.string.command_video))) {
            speakOut(getString(R.string.voice_response_ok))
            Navigation.findNavController(
                requireActivity(),
                R.id.fragment_container
            )
                .navigate(R.id.fragment_video)
        } else if (intention.contains(getString(R.string.command_music))) {
            speakOut(getString(R.string.voice_response_ok))
            Navigation.findNavController(
                requireActivity(),
                R.id.fragment_container
            )
                .navigate(R.id.fragment_music)
        } else if (intention.contains(getString(R.string.command_audio))) {
            speakOut(getString(R.string.voice_response_ok))
            Navigation.findNavController(
                requireActivity(),
                R.id.fragment_container
            )
                .navigate(R.id.fragment_audio)
        } else {
            speakOut(getString(R.string.voice_response_command_not_exist))
            startListening()
        }
    }

    override fun onDestroy() {
        speechRecognizer.stopListening()
        speechRecognizer.destroy()
        textToSpeech.stop()
        textToSpeech.shutdown()
        super.onDestroy()
    }

}