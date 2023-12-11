package com.iesam.rememora.features.music.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.SimpleExoPlayer
import com.iesam.rememora.databinding.FragmentMusicBinding
import com.iesam.rememora.features.music.data.MusicDataRepository
import com.iesam.rememora.features.music.data.MusicRemoteDataSource
import com.iesam.rememora.features.music.domain.GetMusicListUseCase
import com.iesam.rememora.features.music.domain.Music

class MusicPlayerFragment : Fragment() {
    private var _binding: FragmentMusicBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MusicPlayerViewModel by lazy {
        MusicPlayerViewModel(GetMusicListUseCase(MusicDataRepository(MusicRemoteDataSource())))
    }
    private var exoPlayer: SimpleExoPlayer? = null
    private var currentIndex = 0
    private var musicList: List<Music> = mutableListOf()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMusicBinding.inflate(inflater, container, false)
        setupView()
        initializePlayer()
        return binding.root
    }

    private fun initializePlayer() {
        exoPlayer = SimpleExoPlayer.Builder(requireContext())
            .build()
            .also { exoPlayer ->
                binding.musicView.player = exoPlayer
            }
    }

    private fun setupView() {
        binding.mediaControls.apply {

            nextButton.setOnClickListener {
                playNextMusic()
            }
            repeatButton.setOnClickListener {
                playMusic()
            }
            backButton.setOnClickListener {
                playPreviousMusic()
            }
        }

    }

    private fun playNextMusic() {
        if (currentIndex < musicList.size - 1) {
            currentIndex++
            playMusic()
        }
    }

    private fun playPreviousMusic() {
        if (currentIndex > 0) {
            currentIndex--
            playMusic()
        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupObserver()
        viewModel.loadMusicList()
    }


    private fun setupObserver() {
        val observer = Observer<MusicPlayerViewModel.UiState> {
            it.musicList?.let {
                musicList = it
                playMusic()
            }
        }
        viewModel.uiState.observe(viewLifecycleOwner, observer)
    }

    private fun playMusic() {
        if (currentIndex < musicList.size) {
            val currentMusic = musicList[currentIndex]
            val mediaItem = MediaItem.fromUri(currentMusic.source!!)

            exoPlayer?.setMediaItem(mediaItem)
            exoPlayer?.prepare()
            exoPlayer?.play()

            changeMusicName()
        }
    }

    private fun changeMusicName() {
        binding.musicName.text = musicList[currentIndex].title
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
