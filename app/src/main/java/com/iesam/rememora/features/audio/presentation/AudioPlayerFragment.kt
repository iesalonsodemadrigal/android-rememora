package com.iesam.rememora.features.audio.presentation

import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.iesam.rememora.R
import com.iesam.rememora.databinding.FragmentAudioBinding

class AudioPlayerFragment : Fragment() {
    private var _binding: FragmentAudioBinding? = null
    private val binding get() = _binding!!

    private var mediaPlayer: MediaPlayer? = null
    private val handler = Handler(Looper.getMainLooper())
    private var isPlaying = false
    private val updateSeekBar = object : Runnable {
        override fun run() {
            updateSeekBar()
        }
    }

    private val audioResources = listOf(
        R.raw.audio3,
        R.raw.audio4,
        R.raw.audio5,
        R.raw.saludo,
        R.raw.mario_die
    )
    private var currentIndex = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAudioBinding.inflate(inflater, container, false)
        setupView()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupMediaPlayer()

    }

    private fun setupMediaPlayer() {
        mediaPlayer = MediaPlayer.create(requireContext(), audioResources[currentIndex])
        changeAudio()
        binding.apply {
            seekBar.max = mediaPlayer?.duration ?: 0
            seekBar.setOnSeekBarChangeListener(object :
                android.widget.SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(
                    seekBar: android.widget.SeekBar?,
                    progress: Int,
                    fromUser: Boolean
                ) {
                    if (fromUser) {
                        mediaPlayer?.seekTo(progress)
                    }
                }

                override fun onStartTrackingTouch(seekBar: android.widget.SeekBar?) {
                    handler.removeCallbacks(updateSeekBar)
                }

                override fun onStopTrackingTouch(seekBar: android.widget.SeekBar?) {
                    handler.post(updateSeekBar)
                }
            })
        }

        mediaPlayer?.setOnCompletionListener {
            stopAudio()
        }
    }


    private fun setupView() {
        binding.mediaControls.apply {
            nextButton.setOnClickListener {
                playNextAudio()
            }
            repeatButton.setOnClickListener {
                repeatAudio()
            }
            backButton.setOnClickListener {
                playPreviousAudio()
            }

        }
    }

    private fun playOrPauseAudio() {
        if (isPlaying) {
            mediaPlayer?.pause()
        } else {
            mediaPlayer?.start()
            handler.post(updateSeekBar)
        }
        isPlaying = !isPlaying
    }

    private fun repeatAudio() {
        stopAudio()
        playOrPauseAudio()
    }

    private fun stopAudio() {
        mediaPlayer?.stop()
        mediaPlayer?.prepare()
        mediaPlayer?.seekTo(0)
        isPlaying = false
    }

    private fun playNextAudio() {
        if (currentIndex < audioResources.size - 1) {
            currentIndex++
            changeAudio()
        }
    }

    private fun playPreviousAudio() {
        if (currentIndex > 0) {
            currentIndex--
            changeAudio()
        }
    }

    private fun changeAudio() {
        stopAudio()
        mediaPlayer = MediaPlayer.create(requireContext(), audioResources[currentIndex])
        binding.seekBar.max = mediaPlayer?.duration ?: 0
        playOrPauseAudio()
        changeAudioName()
    }

    private fun updateSeekBar() {
        binding.seekBar.progress = mediaPlayer?.currentPosition ?: 0
        handler.postDelayed(updateSeekBar, 10)
    }

    private fun changeAudioName() {
        val audioName = resources.getResourceEntryName(audioResources[currentIndex])
        binding.audioName.text = audioName
    }

    private fun stopMediaPlayer() {
        mediaPlayer?.release()
        mediaPlayer = null
        handler.removeCallbacks(updateSeekBar)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        stopMediaPlayer()
        _binding = null
    }
}
