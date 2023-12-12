package com.iesam.rememora.app.presentation.views

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.ui.StyledPlayerView
import com.iesam.rememora.R
import com.iesam.rememora.databinding.ViewMediaBinding

class MediaPlayerView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : FrameLayout(context, attrs) {
    private val binding = ViewMediaBinding.inflate(LayoutInflater.from(context), this, true)
    private var exoPlayer: SimpleExoPlayer? = null
    private var urlMediaList: List<String> = mutableListOf()
    private var currentIndex = 0


    init {
        setupView()
    }

    fun setupMediaPlayer(exoPlayerView: StyledPlayerView, mediaList: List<String>) {
        initializePlayer(exoPlayerView)
        urlMediaList = mediaList
        playMusic()


    }

    private fun initializePlayer(exoPlayerView: StyledPlayerView) {
        exoPlayer = SimpleExoPlayer.Builder(context).build()
        exoPlayerView.player = exoPlayer
    }

    private fun playMusic() {
        if (currentIndex < urlMediaList.size) {
            val currentMusic = urlMediaList[currentIndex]
            val mediaItem = MediaItem.fromUri(currentMusic)

            exoPlayer?.setMediaItem(mediaItem)
            exoPlayer?.prepare()
            exoPlayer?.play()


        }
    }

    private fun setupView() {
        Log.d("@DEV", "setupview")

        binding.apply {
            isClickable = true
            setOnClickListener {
                Log.d("@DEV", "view")

            }
            backButton.setOnClickListener {
                playPreviousMusic()
            }
            nextButton.setOnClickListener {
                playNextMusic()
            }
            playPauseButton.setOnClickListener {
                isClickable = true
                isEnabled = true
                Log.d("@DEV", "pause")
                togglePlayPause()
            }
        }
    }

    private fun playNextMusic() {
        if (currentIndex < urlMediaList.size - 1) {
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

    private fun togglePlayPause() {
        exoPlayer?.let {
            if (it.isPlaying) {
                it.pause()
                binding.playPauseButton.text = context.getString(R.string.label_buttom_play)
            } else {
                it.play()
                binding.playPauseButton.text = context.getString(R.string.label_buttom_pause)

            }
        }


    }


}