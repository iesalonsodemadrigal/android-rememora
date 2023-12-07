package com.iesam.rememora.features.audio.presentation

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.SimpleExoPlayer
import com.iesam.rememora.app.domain.ErrorApp
import com.iesam.rememora.databinding.FragmentAudioBinding
import com.iesam.rememora.features.audio.data.RemoteDataSource
import com.iesam.rememora.features.audio.domain.Audio
import com.iesam.rememora.features.audio.domain.GetAudiosUseCase

class AudioPlayerFragment : Fragment() {
    private var _binding: FragmentAudioBinding? = null
    private val binding get() = _binding!!
    private val viewModel: AudioPlayerViewModel by lazy {
        AudioPlayerViewModel(
            GetAudiosUseCase(RemoteDataSource())
        )
    }

    private var audiosList : List<Audio> = listOf( )

    private lateinit var player: SimpleExoPlayer
    private var audioIndex = 0


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
            backButton.setOnClickListener{
                playPreviousSong()
            }
            nextButton.setOnClickListener{
                playNextSong()
            }
        }
    }
    private fun playPreviousSong() {
        if (audioIndex > 0) {
            audioIndex--
        }
        prepareAndPlaySong(audiosList[audioIndex].source!!)
    }
    private fun playNextSong() {
        if (audioIndex < audiosList.size - 1) {
            audioIndex++
        }
        prepareAndPlaySong(audiosList[audioIndex].source!!)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupObserver()
        viewModel.getListAudios()
    }

    private fun setupObserver() {
        val observer = Observer<AudioPlayerViewModel.UiState> {
            if (it.isLoading) {

            } else {
                if (it.errorApp != null) {
                    showError(it.errorApp)
                } else {
                    it.audios?.apply {
                        audiosList = this
                        initializePlayer()
                    }
                }
            }
        }
        viewModel.uiState.observe(viewLifecycleOwner, observer)
    }

    private fun showError(error: ErrorApp) {
    }

    private fun initializePlayer() {
        player = SimpleExoPlayer.Builder(requireContext()).build()
        binding.playerView.player = player
        prepareAndPlaySong(audiosList[audioIndex].source!!)
    }
    private fun prepareAndPlaySong(songUrl: String) {
        player.stop()
        player.clearMediaItems()

        val mediaItem = MediaItem.fromUri(Uri.parse(songUrl))
        player.setMediaItem(mediaItem)
        player.prepare()
        player.play()
        binding.audioName.text = audiosList[audioIndex].title
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        player.release()
    }
}
