package com.iesam.rememora.features.images.presentation

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.speech.RecognizerIntent
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.OptIn
import androidx.camera.core.CameraSelector
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.FaceDetection
import com.google.mlkit.vision.face.FaceDetectorOptions
import com.iesam.rememora.R
import com.iesam.rememora.app.extensions.hide
import com.iesam.rememora.app.extensions.show
import com.iesam.rememora.app.presentation.error.ErrorUiModel
import com.iesam.rememora.databinding.FragmentImagesBinding
import com.iesam.rememora.features.images.domain.Image
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors


@AndroidEntryPoint
class ImagePlayerFragment : Fragment() {
    private var _binding: FragmentImagesBinding? = null

    private val binding get() = _binding!!

    private val viewModel by viewModels<ImagePlayerViewModel>()

    private var images: List<Image> = listOf()

    private var numImage = 0

    private lateinit var textToSpeech: TextToSpeech

    private lateinit var cameraExecutor: ExecutorService

    private lateinit var preview: Preview

    private lateinit var cameraProvider: ProcessCameraProvider

    private lateinit var imageAnalyzer: ImageAnalysis

    private val MS_DELAY_TAKE_PHOTO: Long = 2000 //Milisegundos

    private val resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                val data: Intent? = it.data
                if (data != null) {
                    val result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
                    val spokenText = result?.get(0) ?: ""
                    if (spokenText == getString(R.string.command_next)) {
                        if (numImage == (images.size - 1)) {
                            val response = getString(R.string.voice_response_last_picture)
                            speakOut(response)
                        } else {
                            nextImage()
                        }
                    } else if (spokenText == getString(R.string.command_previous)) {
                        if (numImage == 0) {
                            val response = getString(R.string.voice_response_first_picture)
                            speakOut(response)
                        } else {
                            backImage()
                        }
                    } else if (spokenText == getString(R.string.command_video)) {
                        Navigation.findNavController(requireActivity(), R.id.fragment_container)
                            .navigate(R.id.fragment_video)
                    } else if (spokenText == getString(R.string.command_music)) {
                        Navigation.findNavController(requireActivity(), R.id.fragment_container)
                            .navigate(R.id.fragment_music)
                    } else if (spokenText == getString(R.string.command_audio)) {
                        Navigation.findNavController(requireActivity(), R.id.fragment_container)
                            .navigate(R.id.fragment_audio)
                    } else if (spokenText == getString(R.string.command_photos)) {
                        speakOut(getString(R.string.voice_response_fragment_photo))
                    } else {
                        val response = getString(R.string.voice_response_command_not_exist)
                        speakOut(response)
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
        binding.mediaControls.microButton.setOnClickListener {
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
        cameraExecutor = Executors.newSingleThreadExecutor()
        attachCamera()
        setupObserver()
        viewModel.getImages()
        textToSpeech = TextToSpeech(requireContext()) { status ->
            if (status == TextToSpeech.SUCCESS) {
                Locale(getString(R.string.language), getString(R.string.country))
            }
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
            if (it.emotion != null) {
                viewModel.saveImage(images[numImage], it.emotion!!)
                it.emotion = null
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
        attachCamera()
    }

    private fun nextImage() {
        if (numImage < (images.size - 1)) {
            numImage++
        }
        refreshImage()
        updateButtons()
        attachCamera()
    }

    private fun refreshImage() {
        Glide.with(this)
            .load(images[numImage].source)
            .into(binding.image)
        //setupML()
        cameraExecutor = Executors.newSingleThreadExecutor()
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
        super.onDestroyView()
        _binding = null
        textToSpeech.stop()
        textToSpeech.shutdown()
    }

    private fun attachCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())
        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed({
            cameraProviderFuture.addListener({
                // Used to bind the lifecycle of cameras to the lifecycle owner
                cameraProvider = cameraProviderFuture.get()

                // Preview
                preview = Preview.Builder()
                    .build()
                    .also {
                        it.setSurfaceProvider(binding.viewFinder.surfaceProvider)
                    }

                imageAnalyzer = ImageAnalysis.Builder()
                    //.setResolutionSelector(ResolutionSelector.PREFER_CAPTURE_RATE_OVER_HIGHER_RESOLUTION)
                    .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                    .build()
                    .also {
                        it.setAnalyzer(cameraExecutor, EmotionAnalyzer())
                    }

                try {
                    // Unbind use cases before rebinding
                    cameraProvider.unbindAll()

                    // Bind use cases to camera
                    cameraProvider.bindToLifecycle(
                        this,
                        CameraSelector.DEFAULT_FRONT_CAMERA,
                        preview,
                        ImageCapture.Builder().build(),
                        imageAnalyzer
                    )

                } catch (exc: Exception) {
                    Log.e("dev", "Use case binding failed", exc)
                }

            }, ContextCompat.getMainExecutor(requireContext()))
        }, MS_DELAY_TAKE_PHOTO)
    }

    private inner class EmotionAnalyzer : ImageAnalysis.Analyzer {

        // Real-time contour detection
        val realTimeOpts = FaceDetectorOptions.Builder()
            .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_FAST)
            .setLandmarkMode(FaceDetectorOptions.LANDMARK_MODE_NONE)
            .setClassificationMode(FaceDetectorOptions.CLASSIFICATION_MODE_ALL)
            .setContourMode(FaceDetectorOptions.CONTOUR_MODE_NONE)
            .build()

        val faceDetector = FaceDetection.getClient(realTimeOpts)

        @OptIn(ExperimentalGetImage::class)
        override fun analyze(imageProxy: ImageProxy) {
            val mediaImage = imageProxy.image

            if (mediaImage != null) {
                val image =
                    InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)
                // Pass image to an ML Kit Vision API
                faceDetector.process(image)
                    .addOnSuccessListener { faces ->
                        if (faces.size > 0) {
                            viewModel.setFaceEmotion(
                                faces.first().smilingProbability ?: 0f,
                                faces.first().leftEyeOpenProbability ?: 0f,
                                faces.first().rightEyeOpenProbability ?: 0f
                            )
                            cameraExecutor.shutdown()
                        }
                    }
                    .addOnFailureListener { e ->
                        e.message
                    }
                    .addOnCompleteListener {
                        imageProxy.close()
                        imageAnalyzer.clearAnalyzer()
                    }
            } else {
                imageProxy.close()
                imageAnalyzer.clearAnalyzer()
            }
        }
    }
}