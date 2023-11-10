package com.iesam.rememora.features.images.presentation


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.DrawableRes
import androidx.fragment.app.Fragment
import com.iesam.rememora.R
import com.iesam.rememora.databinding.FragmentImagesBinding

class ImagePlayerFragment : Fragment() {

    private var numImage=0

    private val imagen1=R.drawable.rememora1
    private val imagen2=R.drawable.rememora2
    private val imagen3=R.drawable.rememora3
    private val imagen4=R.drawable.rememora4

    private val images: MutableList<Int> = mutableListOf(imagen1,imagen2,imagen3,imagen4)

    private var _binding: FragmentImagesBinding? = null
    private val binding get() = _binding!!

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
            backButton.setOnClickListener{
                backImage()
            }
            nextButton.setOnClickListener{
                nextImage()
            }
            repeatButton.setOnClickListener{
                firstImage()
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        refresImage(images[numImage])
    }

    private fun firstImage() {
        numImage=0
        refresImage(images[numImage])
    }

    private fun backImage() {
        if (numImage>0){
            numImage--
        }
        refresImage(images[numImage])
    }

    private fun nextImage() {
        if (numImage<(images.size-1)){
            numImage++
        }
        refresImage(images[numImage])
    }

    private fun refresImage(@DrawableRes image:Int){
        binding.image.setImageResource(image)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}