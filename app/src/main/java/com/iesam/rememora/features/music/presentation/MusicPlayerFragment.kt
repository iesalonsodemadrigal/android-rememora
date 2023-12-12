package com.iesam.rememora.features.music.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
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
        return binding.root
    }

    private fun setupView() {

    }

    fun initializePlayer() {
        exoPlayer = SimpleExoPlayer.Builder(requireContext()).build()
        binding.musicView.player = exoPlayer
    }


    private fun playNextMusic() {
        if (currentIndex < musicList.size - 1) {
            currentIndex++

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
                val urlList: List<String> = it.map {
                    it.source ?: ""
                }
                binding.mediaPlayer.setupMediaPlayer(binding.musicView, urlList)

            }
        }
        viewModel.uiState.observe(viewLifecycleOwner, observer)
    }



    private fun changeMusicName() {
        binding.musicName.text = musicList[currentIndex].title
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
