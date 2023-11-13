package com.iesam.rememora.features.music.presentation

import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.iesam.rememora.R
import com.iesam.rememora.databinding.FragmentMusicBinding

class MusicPlayerFragment : Fragment() {
    private var _binding: FragmentMusicBinding? = null
    private val binding get() = _binding!!

    private var mediaPlayer: MediaPlayer? = null
    private val handler = Handler(Looper.getMainLooper())
    private var isPlaying = false
    private val updateSeekBar = object : Runnable {
        override fun run() {
            updateSeekBar()
        }
    }

    private val MusicResources = listOf(
        R.raw.la_casa_por_el_tejado,
        R.raw.me_equivocaria_otra_vez,
        R.raw.por_la_boca_vive_el_pez,
        R.raw.soldadito_marinero
    )
    private var currentIndex = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMusicBinding.inflate(inflater, container, false)
        setupView()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupMediaPlayer()

    }

    private fun setupMediaPlayer() {
        mediaPlayer = MediaPlayer.create(requireContext(), MusicResources[currentIndex])
        changeMusic()
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
            stopMusic()
        }
    }


    private fun setupView() {
        binding.mediaControls.apply {
            nextButton.setOnClickListener {
                playNextMusic()
            }
            repeatButton.setOnClickListener {
                repeatMusic()
            }
            backButton.setOnClickListener {
                playPreviousMusic()
            }

        }
    }

    private fun playOrPauseMusic() {
        if (isPlaying) {
            mediaPlayer?.pause()
        } else {
            mediaPlayer?.start()
            handler.post(updateSeekBar)
        }
        isPlaying = !isPlaying
    }

    private fun repeatMusic() {
        stopMusic()
        playOrPauseMusic()
    }

    private fun stopMusic() {
        mediaPlayer?.stop()
        mediaPlayer?.prepare()
        mediaPlayer?.seekTo(0)
        isPlaying = false
    }

    private fun playNextMusic() {
        if (currentIndex < MusicResources.size - 1) {
            currentIndex++
            changeMusic()
        }
    }

    private fun playPreviousMusic() {
        if (currentIndex > 0) {
            currentIndex--
            changeMusic()
        }
    }

    private fun changeMusic() {
        stopMusic()
        mediaPlayer = MediaPlayer.create(requireContext(), MusicResources[currentIndex])
        binding.seekBar.max = mediaPlayer?.duration ?: 0
        playOrPauseMusic()
        changeMusicName()
    }

    private fun updateSeekBar() {
        binding.seekBar.progress = mediaPlayer?.currentPosition ?: 0
        handler.postDelayed(updateSeekBar, 10)
    }

    private fun changeMusicName() {
        val MusicName = resources.getResourceEntryName(MusicResources[currentIndex])
        binding.musicName.text = MusicName
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