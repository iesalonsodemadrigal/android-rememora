package com.iesam.rememora.features.images.presentation

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
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.iesam.rememora.R
import com.iesam.rememora.app.extensions.hide
import com.iesam.rememora.app.extensions.show
import com.iesam.rememora.app.presentation.error.ErrorUiModel
import com.iesam.rememora.databinding.FragmentImagesBinding
import com.iesam.rememora.features.images.domain.Image
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale

@AndroidEntryPoint
class ImagePlayerFragment : Fragment() {
    private var _binding: FragmentImagesBinding? = null

    private val binding get() = _binding!!

    private val viewModel by viewModels<ImagePlayerViewModel>()

    private var images: List<Image> = listOf()

    private var numImage = 0

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
                    handleResult(spokenText)
                }
            }
        }

    private fun handleResult(command: String) {
        if (command.contains(getString(R.string.command_next))) {
            if (numImage == (images.size - 1)) {
                val response = getString(R.string.voice_response_last_picture)
                speakOut(response)
            } else {
                speakOut(getString(R.string.voice_response_ok))
                nextImage()
            }
            startListening()
        } else if (command.contains(getString(R.string.command_previous))) {
            if (numImage == 0) {
                speakOut(getString(R.string.voice_response_first_picture))
            } else {
                speakOut(getString(R.string.voice_response_ok))
                backImage()
            }
            startListening()
        } else if (command.contains(getString(R.string.command_video))) {
            speakOut(getString(R.string.voice_response_ok))
            Navigation.findNavController(requireActivity(), R.id.fragment_container)
                .navigate(R.id.fragment_video)
        } else if (command.contains(getString(R.string.command_music))) {
            speakOut(getString(R.string.voice_response_ok))
            Navigation.findNavController(requireActivity(), R.id.fragment_container)
                .navigate(R.id.fragment_music)
        } else if (command.contains(getString(R.string.command_audio))) {
            speakOut(getString(R.string.voice_response_ok))
            Navigation.findNavController(requireActivity(), R.id.fragment_container)
                .navigate(R.id.fragment_audio)
        } else {
            speakOut(getString(R.string.voice_response_command_not_exist))
            startListening()
        }
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
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "es")
        speechRecognizer.startListening(intent)
    }

    private fun speakOut(text: String) {
        textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null, "")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentImagesBinding.inflate(inflater, container, false)
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
                        handleResult(command)
                    } else {
                        startListening()
                    }
                }
            }

            override fun onPartialResults(partialResults: Bundle) {}
            override fun onEvent(eventType: Int, params: Bundle) {}
        })
    }

    private fun setupView() {
        binding.apply {
            mediaControls.backButton.setOnClickListener {
                backImage()
            }
            mediaControls.nextButton.setOnClickListener {
                nextImage()
            }
        }
        binding.mediaControls.microButton.setOnClickListener {
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
        setupObserver()
        viewModel.getImages()

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
        val observer = Observer<ImagePlayerViewModel.UiState> {
            if (it.isLoading) {

            } else {
                if (it.errorApp != null) {
                    showError(it.errorApp)
                } else {
                    it.images?.apply {
                        images = this
                        binding.apply {
                            image.show()
                            mediaControls.menuBottom.show()
                        }
                        refreshImage()
                        updateButtons()
                    }
                }
            }
        }
        viewModel.uiState.observe(viewLifecycleOwner, observer)
    }

    private fun showError(error: ErrorUiModel) {
        binding.apply {
            image.hide()
            mediaControls.menuBottom.hide()
        }
        binding.errorView.render(error)
    }

    private fun firstImage() {
        numImage = 0
        refreshImage()
        updateButtons()
    }

    private fun backImage() {
        if (numImage > 0) {
            numImage--
        }
        refreshImage()
        updateButtons()
    }

    private fun nextImage() {
        if (numImage < (images.size - 1)) {
            numImage++
        }
        refreshImage()
        updateButtons()
    }

    private fun refreshImage() {
        Glide.with(this)
            .load(images[numImage].source)
            .into(binding.image)
    }

    private fun updateButtons() {
        binding.apply {
            if (numImage == 0 && images.size > 0) {
                mediaControls.nextButton.isEnabled = true
                mediaControls.backButton.isEnabled = false
            } else if (images.size > 0 && numImage == images.size - 1) {
                mediaControls.nextButton.isEnabled = false
                mediaControls.backButton.isEnabled = true
            } else {
                mediaControls.nextButton.isEnabled = true
                mediaControls.backButton.isEnabled = true
            }
        }
    }

    override fun onDestroyView() {
        _binding = null
        textToSpeech.stop()
        textToSpeech.shutdown()
        speechRecognizer.stopListening()
        speechRecognizer.destroy()
        super.onDestroyView()
    }

}