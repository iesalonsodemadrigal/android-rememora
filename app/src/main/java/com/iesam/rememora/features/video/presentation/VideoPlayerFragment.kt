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
            //Pasar a un string
            val prompt = "Tengo una aplicación llamada Rememora que sirve para ver ver diferente " +
                    "contenido multimedia. A la izquierda hay un menú para navegar hacia el " +
                    "diferente contenido con un botón para cada uno, es decir, fotos, vídeos, música o audios. En el centro" +
                    " se visualiza el contenido y en la parte inferior hay otro menú con cuatro" +
                    " botones: siguiente y anterior, para pasar, por ejemplo, de un video a otro" +
                    ", un botón reproducir y otro pausar.\n" +
                    "Quiero saber cuál es el botón que debe pulsar el usuario en la aplicación si" +
                    " se encuentra viendo VIDEOS y quiere lo siguiente: \"${phrase}\".\n" +
                    "Tu respuesta debe ser el nombre del botón o una pregunta o frase corta si hay" +
                    " confusión. Es importante que te ciñas a este tipo de respuesta, no me" +
                    " muestres más información."

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
                        //binding.mediaPlayer.handleResult(intention)
                        binding.mediaPlayer.speakOut(intention)
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
