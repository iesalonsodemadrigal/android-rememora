package com.iesam.rememora.features.video.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.iesam.rememora.R
import com.iesam.rememora.app.extensions.hide
import com.iesam.rememora.app.extensions.show
import com.iesam.rememora.app.presentation.error.ErrorUiModel
import com.iesam.rememora.databinding.FragmentVideosBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class VideoPlayerFragment : Fragment() {
    private val viewModel by viewModels<VideoPlayerViewModel>()
    private var _binding: FragmentVideosBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentVideosBinding.inflate(inflater, container, false)
        binding.mediaPlayer.setFragment(this, getString(R.string.fragment_name_video))
        binding.mediaPlayer.setOnCustomEventListener { phrase: String ->
            val prompt = getString(R.string.prompt_videos, phrase)

            viewModel.getIntention(prompt)
        }
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupObserver()
        viewModel.getVideos()
    }

    private fun setupObserver() {
        val observer = Observer<VideoPlayerViewModel.UiState> { it ->
            if (!it.isLoading) {
                if (it.errorApp == null) {
                    it.videos?.let { videos ->
                        val videoList: List<String> = videos.map {
                            it.source!!
                        }
                        binding.mediaPlayer.show()
                        binding.mediaPlayer.render(videoList)
                    }
                    it.intention?.let { intention ->
                        binding.mediaPlayer.handleResult(intention.lowercase())
                    }
                }
            }
            it.errorApp?.let { error ->
                showError(error)
            }
        }
        viewModel.uiState.observe(viewLifecycleOwner, observer)
    }

    private fun showError(error: ErrorUiModel) {
        binding.mediaPlayer.hide()
        binding.errorView.render(error)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
