package com.iesam.rememora.app.presentation.views

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.speech.RecognizerIntent
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

class MediaPlayerView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : FrameLayout(context, attrs) {

    private val binding = ViewMediaplayerBinding.inflate(LayoutInflater.from(context), this, true)
    private var urlMediaList: List<String> = mutableListOf()
    private var currentPosition = 0

    private var exoPlayer: ExoPlayer

    private var fragment: Fragment? = null

    init {
        exoPlayer = ExoPlayer.Builder(context).build()
        binding.mediaView.player = exoPlayer
        setupView()
    }

    private var resultLauncher: ActivityResultLauncher<Intent>? = null

    fun setFragment(fragment: Fragment) {
        this.fragment = fragment
        this.resultLauncher =
            fragment.registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                if (it.resultCode == Activity.RESULT_OK) {
                    val data: Intent? = it.data
                    if (data != null) {
                        val result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
                        val spokenText = result?.get(0) ?: ""
                        when (spokenText) {
                            context.getString(R.string.command_next) -> playNextMedia()
                            context.getString(R.string.command_previous) -> playPreviousMedia()
                            context.getString(R.string.command_play) -> {
                                if (!exoPlayer.isPlaying) {
                                    playMusic()
                                }
                            }

                            context.getString(R.string.command_pause) -> {
                                if (exoPlayer.isPlaying) {
                                    pause()
                                }
                            }

                            context.getString(R.string.command_photos) -> {
                                Navigation.findNavController(
                                    fragment.requireActivity(),
                                    R.id.fragment_container
                                )
                                    .navigate(R.id.fragment_imagen)
                            }

                            context.getString(R.string.command_music) -> {
                                Navigation.findNavController(
                                    fragment.requireActivity(),
                                    R.id.fragment_container
                                )
                                    .navigate(R.id.fragment_music)
                            }

                            context.getString(R.string.command_video) -> {
                                Navigation.findNavController(
                                    fragment.requireActivity(),
                                    R.id.fragment_container
                                )
                                    .navigate(R.id.fragment_video)
                            }

                            context.getString(R.string.command_audio) -> {
                                Navigation.findNavController(
                                    fragment.requireActivity(),
                                    R.id.fragment_container
                                )
                                    .navigate(R.id.fragment_audio)
                            }
                        }
                    }
                }
            }
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

    private fun playNextMedia() {
        if (currentPosition < urlMediaList.size - 1) {
            currentPosition++
            playMusic()
        }
    }

    private fun playPreviousMedia() {
        if (currentPosition > 0) {
            currentPosition--
            playMusic()
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

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        exoPlayer.release()
    }
}