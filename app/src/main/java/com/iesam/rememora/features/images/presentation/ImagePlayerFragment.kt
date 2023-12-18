package com.iesam.rememora.features.images.presentation


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.iesam.rememora.app.domain.ErrorApp
import com.iesam.rememora.databinding.FragmentImagesBinding
import com.iesam.rememora.features.images.domain.Image
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ImagePlayerFragment : Fragment() {
    private var _binding: FragmentImagesBinding? = null

    private val binding get() = _binding!!

    private val viewModel by viewModels<ImagePlayerViewModel>()

    private var images: List<Image> = listOf()

    private var numImage = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentImagesBinding.inflate(inflater, container, false)
        setupView()
        return binding.root
    }

    private fun setupView() {
        binding.apply {
            mediaControls.backButton.setOnClickListener {
                backImage()
            }
            mediaControls.nextButton.setOnClickListener {
                nextImage()
            }
            mediaControls.repeatButton.setOnClickListener {
                firstImage()
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupObserver()
        viewModel.getImages()
    }

    private fun setupObserver() {
        val observer = Observer<ImagePlayerViewModel.UiState> {
            if (it.isLoading) {

            } else {
                if (it.errorApp != null) {
                    showError(it.errorApp)
                } else {
                    it.images?.apply {
                        images = this
                        refreshImage()
                    }
                }
            }
        }
        viewModel.uiState.observe(viewLifecycleOwner, observer)
    }

    private fun firstImage() {
        numImage = 0
        refreshImage()
    }

    private fun backImage() {
        if (numImage > 0) {
            numImage--
        }
        refreshImage()
    }

    private fun nextImage() {
        if (numImage < (images.size - 1)) {
            numImage++
        }
        refreshImage()
    }

    private fun showError(error: ErrorApp) {
    }

    private fun refreshImage() {
        Glide.with(this)
            .load(images[numImage].source)
            .into(binding.image)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}