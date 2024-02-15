package com.iesam.rememora.features.images.presentation

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.iesam.rememora.R
import com.iesam.rememora.app.extensions.hide
import com.iesam.rememora.app.extensions.invisible
import com.iesam.rememora.app.extensions.setUrl
import com.iesam.rememora.app.extensions.show
import com.iesam.rememora.app.presentation.error.ErrorUiModel
import com.iesam.rememora.app.presentation.events.OnSwipeTouchListener
import com.iesam.rememora.databinding.FragmentImagesBinding
import com.iesam.rememora.features.home.presentation.HomeActivity
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

    @SuppressLint("ClickableViewAccessibility")
    private fun setupView() {
        (requireActivity() as HomeActivity).showHomeButton()

        binding.apply {
            mediaControls.menuBottom.hide()

            mediaControls.backButton.setOnClickListener {
                backImage()
            }

            actionPrevImage.setOnClickListener {
                backImage()
            }

            imagePrevious.setOnClickListener {
                backImage()
            }

            mediaControls.nextButton.setOnClickListener {
                nextImage()
            }
            actionNextImg.setOnClickListener {
                nextImage()
            }

            imageNext.setOnClickListener {
                nextImage()
            }

            image.setFactory {
                val imageView = ImageView(requireActivity())
                imageView.scaleType = ImageView.ScaleType.CENTER_CROP
                imageView.layoutParams = FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.MATCH_PARENT,
                    FrameLayout.LayoutParams.MATCH_PARENT
                )
                imageView
            }

            layoutMediaPlayer.setOnTouchListener(object : OnSwipeTouchListener() {
                @SuppressLint("ClickableViewAccessibility")
                override fun onSwipeLeft() {
                    nextImage()
                }

                @SuppressLint("ClickableViewAccessibility")
                override fun onSwipeRight() {
                    backImage()
                }
            })
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
                        binding.apply {
                            //image.show()
                            //mediaControls.menuBottom.show()
                        }
                        refreshImage()
                        updateButtons()
                    }
                }
            }
        }
        viewModel.uiState.observe(viewLifecycleOwner, observer)
    }

    private fun showError(error: ErrorUiModel) {
        binding.apply {
            image.hide()
            mediaControls.menuBottom.hide()
        }
        binding.errorView.render(error)
    }

    private fun firstImage() {
        numImage = 0
        refreshImage()
        updateButtons()
    }

    private fun backImage() {
        if (numImage > 0) {
            numImage--
            binding.apply {
                image.setInAnimation(requireContext(), R.anim.from_left)
                image.setOutAnimation(requireContext(), R.anim.to_right)
            }
            refreshImage()
            updateButtons()
        }
    }

    private fun nextImage() {
        if (numImage < (images.size - 1)) {
            numImage++
            binding.apply {
                image.setInAnimation(requireContext(), R.anim.from_right)
                image.setOutAnimation(requireContext(), R.anim.to_left)
            }
            refreshImage()
            updateButtons()
        }
    }

    private fun refreshImage() {
        binding.image.setImageURI(images[numImage].source.toUri())
        bindMiniImages()
        bindLabelNum()
    }

    private fun bindLabelNum() {
        binding.labelNum.text = getString(
            R.string.label_navigation,
            (numImage + 1).toString(),
            (images.size).toString(),
            getString(R.string.label_navigation_photo)
        )
    }

    private fun bindMiniImages() {
        when (numImage) {
            0 -> {
                binding.apply {
                    imagePrevious.invisible()
                    imageNext.show()
                    imageNext.setUrl(images[numImage + 1].source)
                }
            }

            images.size - 1 -> {
                binding.apply {
                    imagePrevious.show()
                    imagePrevious.setUrl(images[numImage - 1].source)
                    imageNext.invisible()
                }
            }

            else -> {
                binding.apply {
                    imagePrevious.show()
                    imagePrevious.setUrl(images[numImage - 1].source)
                    imageNext.show()
                    imageNext.setUrl(images[numImage + 1].source)
                }
            }
        }
    }

    private fun updateButtons() {
        binding.apply {
            if (numImage == 0 && images.size > 0) {
                mediaControls.nextButton.isEnabled = true
                mediaControls.backButton.isEnabled = false
            } else if (images.size > 0 && numImage == images.size - 1) {
                mediaControls.nextButton.isEnabled = false
                mediaControls.backButton.isEnabled = true
            } else {
                mediaControls.nextButton.isEnabled = true
                mediaControls.backButton.isEnabled = true
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}