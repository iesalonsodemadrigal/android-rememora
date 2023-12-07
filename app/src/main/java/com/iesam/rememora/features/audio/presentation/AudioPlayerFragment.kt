package com.iesam.rememora.features.audio.presentation

import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.iesam.rememora.R
import com.iesam.rememora.app.domain.ErrorApp
import com.iesam.rememora.databinding.FragmentAudioBinding
import com.iesam.rememora.features.audio.data.RemoteDataSource
import com.iesam.rememora.features.audio.domain.GetAudiosUseCase

class AudioPlayerFragment : Fragment() {
    private var _binding: FragmentAudioBinding? = null
    private val binding get() = _binding!!
    private val viewModel : AudioPlayerViewModel by lazy {
        AudioPlayerViewModel (
            GetAudiosUseCase(RemoteDataSource())
        )
    }
    private var mediaPlayer: MediaPlayer? = null
    private val handler = Handler(Looper.getMainLooper())
    private var isPlaying = false
    private val audioResources = listOf(
        R.raw.audio3,
        R.raw.audio4,
        R.raw.audio5,
        R.raw.saludo,
        R.raw.mario_die
    )
    private var currentIndex = 0
    private val updateSeekBar = object : Runnable {
        override fun run() {
            updateSeekBar()
        }
    }
    private fun updateSeekBar() {
        binding.seekBar.progress = mediaPlayer?.currentPosition ?: 0
        handler.postDelayed(updateSeekBar, 10)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAudioBinding.inflate(inflater, container, false)
        setupView()
        return binding.root
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
    private fun playNextAudio() {
        if (currentIndex < audioResources.size - 1) {
            currentIndex++
            changeAudio()
        }
    }
    private fun repeatAudio() {
        stopAudio()
        playOrPauseAudio()
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
    private fun stopAudio() {
        mediaPlayer?.stop()
        mediaPlayer?.prepare()
        mediaPlayer?.seekTo(0)
        isPlaying = false
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
    private fun changeAudioName() {
        val audioName = resources.getResourceEntryName(audioResources[currentIndex])
        binding.audioName.text = audioName
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupMediaPlayer()
        setupObserver()
        viewModel.getListAudios()
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
    private fun setupObserver () {
        val observer = Observer<AudioPlayerViewModel.UiState>{
            if (it.isLoading){

            }else{
                if (it.errorApp != null){
                    showError(it.errorApp)
                }else{
                    it.audios?.apply {
                    }
                }
            }
        }
        viewModel.uiState.observe(viewLifecycleOwner, observer)
    }
    private fun showError(error : ErrorApp){
    }


    override fun onDestroyView() {
        super.onDestroyView()
        stopMediaPlayer()
        _binding = null
    }
    private fun stopMediaPlayer() {
        mediaPlayer?.release()
        mediaPlayer = null
        handler.removeCallbacks(updateSeekBar)
    }
}
