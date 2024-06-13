package com.iesam.rememora.features.audio.presentation

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
import com.iesam.rememora.databinding.FragmentAudioBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AudioPlayerFragment : Fragment() {
    private var _binding: FragmentAudioBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<AudioPlayerViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAudioBinding.inflate(inflater, container, false)
        binding.mediaPlayer.setFragment(this, getString(R.string.fragment_name_audio))
        binding.mediaPlayer.setOnCustomEventListener { phrase: String ->
            val prompt = getString(R.string.prompt_audios, phrase)

            viewModel.getIntention(prompt)
        }
        return binding.root
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
                    hideError()
                    it.audios?.let { audios ->
                        val urlListAudios: List<String> = audios.map { audio ->
                            audio.source!!
                        }
                        binding.mediaPlayer.show()
                        binding.mediaPlayer.render(urlListAudios)
                    }
                    it.intention?.let { intention ->
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
