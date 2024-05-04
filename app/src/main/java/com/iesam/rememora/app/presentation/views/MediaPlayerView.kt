package com.iesam.rememora.app.presentation.views

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.speech.tts.TextToSpeech
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.navigation.Navigation
import com.google.android.material.snackbar.Snackbar
import com.iesam.rememora.R
import com.iesam.rememora.databinding.ViewMediaplayerBinding
import java.util.Locale

class MediaPlayerView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : FrameLayout(context, attrs) {

    private val binding = ViewMediaplayerBinding.inflate(LayoutInflater.from(context), this, true)
    private var urlMediaList: List<String> = mutableListOf()
    private var currentPosition = 0

    private var exoPlayer: ExoPlayer

    private var textToSpeech: TextToSpeech

    private var nameFragment: String = ""

    private var fragment: Fragment? = null

    private var resultLauncher: ActivityResultLauncher<Intent>? = null

    private lateinit var speechRecognizer: SpeechRecognizer

    private var destroySpeech = false

    init {
        exoPlayer = ExoPlayer.Builder(context).build()
        binding.mediaView.player = exoPlayer
        textToSpeech = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                Locale("es", "ES")
            }
        }
        createSpeechRecognizer()
        setupView()
        startListening()
    }

    private fun createSpeechRecognizer() {
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(context)
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
                    if (command.contains(context.getString(R.string.keyword_1)) || command.contains(
                            context.getString(R.string.keyword_2)
                        ) || command.contains(
                            context.getString(R.string.keyword_3)
                        ) || command.contains(
                            context.getString(
                                R.string.keyword_4
                            )
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

    fun handleResult(command: String) {
        if (command.contains(context.getString(R.string.command_next))) {
            speakOut(context.getString(R.string.voice_response_ok))
            playNextMedia()
            startListening()
        } else if (command.contains(context.getString(R.string.command_previous))) {
            speakOut(context.getString(R.string.voice_response_ok))
            playPreviousMedia()
            startListening()
        } else if (command.contains(context.getString(R.string.command_play))) {
            if (!exoPlayer.isPlaying) {
                speakOut(context.getString(R.string.voice_response_ok))
                playMusic()
            } else {
                speakOut(context.getString(R.string.voice_response_play))
            }
            startListening()
        } else if (command.contains(context.getString(R.string.command_pause))) {
            if (exoPlayer.isPlaying) {
                speakOut(context.getString(R.string.voice_response_ok))
                pause()
            } else {
                speakOut(context.getString(R.string.voice_response_pause))
            }
            startListening()
        } else if (command.contains(context.getString(R.string.command_photos))) {
            speakOut(context.getString(R.string.voice_response_ok))
            Navigation.findNavController(
                fragment!!.requireActivity(),
                R.id.fragment_container
            )
                .navigate(R.id.fragment_imagen)
        } else if (command.contains(context.getString(R.string.command_music))) {
            if (nameFragment == context.getString(R.string.fragment_name_music)) {
                speakOut(context.getString(R.string.voice_response_fragment_music))
                startListening()
            } else {
                speakOut(context.getString(R.string.voice_response_ok))
                Navigation.findNavController(
                    fragment!!.requireActivity(),
                    R.id.fragment_container
                )
                    .navigate(R.id.fragment_music)
            }
        } else if (command.contains(context.getString(R.string.command_video))) {
            if (nameFragment == context.getString(R.string.fragment_name_video)) {
                speakOut(context.getString(R.string.voice_response_fragment_video))
                startListening()
            } else {
                speakOut(context.getString(R.string.voice_response_ok))
                Navigation.findNavController(
                    fragment!!.requireActivity(),
                    R.id.fragment_container
                )
                    .navigate(R.id.fragment_video)
            }
        } else if (command.contains(context.getString(R.string.command_audio))) {
            if (nameFragment == context.getString(R.string.fragment_name_audio)) {
                speakOut(context.getString(R.string.voice_response_fragment_audio))
                startListening()
            } else {
                speakOut(context.getString(R.string.voice_response_ok))
                Navigation.findNavController(
                    fragment!!.requireActivity(),
                    R.id.fragment_container
                )
                    .navigate(R.id.fragment_audio)
            }
        } else {
            speakOut(context.getString(R.string.voice_response_command_not_exist))
            startListening()
        }
    }

    private fun playNextMedia() {
        if (currentPosition < urlMediaList.size - 1) {
            currentPosition++
            playMusic()
        } else {
            speakOut(context.getString(R.string.voice_response_last))
        }
    }

    private fun playPreviousMedia() {
        if (currentPosition > 0) {
            currentPosition--
            playMusic()
        } else {
            speakOut(context.getString(R.string.voice_response_first))
        }
    }

    private fun playOrPauseMedia() {
        exoPlayer.let {
            if (it.isPlaying) {
                pause()
            } else {
                it.play()
                binding.playPauseButton.text = context.getString(R.string.label_buttom_pause)
            }
        }
    }

    private fun pause() {
        exoPlayer.let {
            it.pause()
            binding.playPauseButton.text = context.getString(R.string.label_buttom_play)
        }
    }

    private fun playMusic() {
        checkList()

        val currentMusic = urlMediaList[currentPosition]
        val mediaItem = MediaItem.fromUri(currentMusic)
        exoPlayer.setMediaItem(mediaItem)
        exoPlayer.prepare()
        exoPlayer.play()
        binding.playPauseButton.text = context.getString(R.string.label_buttom_pause)

    }

    private fun checkList() {
        binding.backButton.isEnabled = currentPosition > 0
        binding.nextButton.isEnabled = currentPosition < urlMediaList.size - 1
    }

    private fun promptSpeechInput() {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        intent.putExtra(
            RecognizerIntent.EXTRA_LANGUAGE_MODEL,
            RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
        )
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "es")
        intent.putExtra(
            RecognizerIntent.EXTRA_PROMPT,
            context.getString(R.string.extra_prompt_recognizer)
        )

        resultLauncher?.launch(intent)

    }

    fun render(mediaList: List<String>) {
        urlMediaList = mediaList
        playMusic()
        pause()
    }

    fun setFragment(fragment: Fragment, name: String) {
        this.nameFragment = name
        this.fragment = fragment
        this.resultLauncher =
            fragment.registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                if (it.resultCode == Activity.RESULT_OK) {
                    val data: Intent? = it.data
                    if (data != null) {
                        val result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
                        val spokenText = result?.get(0) ?: ""
                        handleResult(spokenText)
                    }
                }
            }
    }

    private fun speakOut(text: String) {
        textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null, "")
    }

    private fun setupView() {
        checkList()
        exoPlayer.addListener(object : Player.Listener {
            override fun onPlaybackStateChanged(state: Int) {
                when (state) {
                    Player.STATE_ENDED -> {
                        binding.playPauseButton.text = context.getString(R.string.label_buttom_play)
                        playMusic()
                        exoPlayer.pause()
                        exoPlayer.seekTo(0)
                    }
                }
            }
        })
        binding.apply {
            backButton.setOnClickListener {
                playPreviousMedia()
            }
            nextButton.setOnClickListener {
                playNextMedia()
            }
            playPauseButton.setOnClickListener {
                playOrPauseMedia()
            }
            microButton.setOnClickListener {
                if (ContextCompat.checkSelfPermission(
                        context,
                        Manifest.permission.RECORD_AUDIO
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    speechRecognizer.destroy()
                    destroySpeech = true
                    promptSpeechInput()
                } else {
                    Snackbar.make(
                        binding.root,
                        context.getString(R.string.no_voice_permissions),
                        Snackbar.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    override fun onDetachedFromWindow() {
        exoPlayer.release()
        textToSpeech.stop()
        textToSpeech.shutdown()
        speechRecognizer.stopListening()
        speechRecognizer.destroy()
        super.onDetachedFromWindow()
    }
}