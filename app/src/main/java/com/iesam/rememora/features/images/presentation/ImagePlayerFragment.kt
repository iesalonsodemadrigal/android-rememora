package com.iesam.rememora.features.images.presentation


import android.Manifest
import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.speech.RecognizerIntent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.iesam.rememora.app.extensions.hide
import com.iesam.rememora.app.extensions.show
import com.iesam.rememora.app.presentation.error.ErrorUiModel
import com.iesam.rememora.databinding.FragmentImagesBinding
import com.iesam.rememora.features.images.domain.Image
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ImagePlayerFragment : Fragment() {
    private var _binding: FragmentImagesBinding? = null

    private val binding get() = _binding!!

    private val viewModel by viewModels<ImagePlayerViewModel>()

    private var images: List<Image> = listOf()

    private var numImage = 0

    private val REQ_CODE_SPEECH_INPUT = 100


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentImagesBinding.inflate(inflater, container, false)
        setupView()
        return binding.root
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
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.RECORD_AUDIO
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            binding.mediaControls.microButton.setOnClickListener {
                promptSpeechInput()
            }
        }
    }

    private fun promptSpeechInput() {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        intent.putExtra(
            RecognizerIntent.EXTRA_LANGUAGE_MODEL,
            RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
        )
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "es")
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Di algo...")

        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT)
        } catch (a: ActivityNotFoundException) {
            Toast.makeText(
                requireContext(),
                "Lo siento, tu dispositivo no admite entrada de voz",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupObserver()
        viewModel.getImages()
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            REQ_CODE_SPEECH_INPUT -> {
                if (resultCode == Activity.RESULT_OK && null != data) {
                    val result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
                    val spokenText = result?.get(0) ?: ""
                    if (spokenText == "siguiente") {
                        nextImage()
                    } else if (spokenText == "anterior") {
                        backImage()
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}