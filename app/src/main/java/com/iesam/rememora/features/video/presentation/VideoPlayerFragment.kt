package com.iesam.rememora.features.video.presentation

import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.VideoView
import androidx.fragment.app.Fragment
import com.iesam.rememora.R
import com.iesam.rememora.databinding.FragmentVideosBinding

class VideoPlayerFragment : Fragment(){

    private lateinit var mediaPlayer: MediaPlayer
    private lateinit var video: VideoView

    private val videos = intArrayOf(R.raw.video1, R.raw.video2, R.raw.video3)
    private var posVideo = 0

    private var _binding: FragmentVideosBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentVideosBinding.inflate(inflater, container, false)
        setupView()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mediaPlayer = MediaPlayer()
        video = view.findViewById(R.id.video)
        videoSet(posVideo)
        video.start()
    }

    private fun setupView() {
        binding.apply {
            mediaControls.backButton.setOnClickListener{
                backVideo()
            }
            mediaControls.nextButton.setOnClickListener{
                nextVideo()
            }
            mediaControls.repeatButton.setOnClickListener{
                replay()
            }
        }
    }

    private fun replay() {
        video.seekTo(0)
    }

    private fun backVideo() {
        if (posVideo > 0){
            posVideo--
        }
        videoSet(posVideo)
    }

    private fun nextVideo() {
        if (posVideo < (videos.size - 1)){
            posVideo++
        }
        videoSet(posVideo)
    }

    private fun videoSet(pos : Int){
        binding.video.setVideoURI((Uri.parse("android.resource://"+requireActivity().packageName+"/"+videos[pos])))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mediaPlayer.release()
        _binding = null
    }

}
