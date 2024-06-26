package com.iesam.rememora.features.music.presentation

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
import com.iesam.rememora.databinding.FragmentMusicBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MusicPlayerFragment : Fragment() {
    private var _binding: FragmentMusicBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<MusicPlayerViewModel>()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMusicBinding.inflate(inflater, container, false)
        binding.mediaPlayer.setFragment(this, getString(R.string.fragment_name_music))
        binding.mediaPlayer.setOnCustomEventListener { phrase: String ->
            val prompt = getString(R.string.prompt_music, phrase)

            viewModel.getIntention(prompt)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupObserver()
        viewModel.loadMusicList()
    }


    private fun setupObserver() {
        val observer = Observer<MusicPlayerViewModel.UiState> { uiState ->
            if (uiState.isLoading) {

            } else {
                if (uiState.errorApp != null) {
                    showError(uiState.errorApp)
                } else {
                    hideError()
                    uiState.musicList?.let { musicList ->
                        val urlList: List<String> = musicList.map {
                            it.source
                        }
                        binding.mediaPlayer.show()
                        binding.mediaPlayer.render(urlList)

                    }
                    uiState.intention?.let { intention ->
                        binding.mediaPlayer.handleResult(intention.lowercase())
                    }
                }
            }
        }
        viewModel.uiState.observe(viewLifecycleOwner, observer)
    }

    private fun showError(error: ErrorUiModel) {
        binding.mediaPlayer.hide()
        binding.errorView.render(error)
    }

    private fun hideError() {
        binding.errorView.hide()
        binding.mediaPlayer.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
