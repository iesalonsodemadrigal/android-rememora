package com.iesam.rememora.app.presentation.views

import android.content.Context
import android.util.AttributeSet
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

    fun render(exoPlayerView: StyledPlayerView, mediaList: List<String>) {
        initializePlayer(exoPlayerView)
        urlMediaList = mediaList
        playMusic()


    }

    private fun initializePlayer(exoPlayerView: StyledPlayerView) {
        exoPlayer = SimpleExoPlayer.Builder(context).build()
        exoPlayerView.player = exoPlayer
    }

    private fun playMusic() {
        checkList()
        if (currentIndex < urlMediaList.size) {
            val currentMusic = urlMediaList[currentIndex]
            val mediaItem = MediaItem.fromUri(currentMusic)
            exoPlayer?.setMediaItem(mediaItem)
            exoPlayer?.prepare()
            exoPlayer?.play()


        }
    }

    private fun setupView() {
        checkList()
        binding.apply {
            backButton.setOnClickListener {
                playPreviousMedia()
            }
            nextButton.setOnClickListener {
                playNextMedia()
            }
            playPauseButton.setOnClickListener {
                playOrPauseMedia()
            }
        }
    }

    private fun checkList() {
        binding.backButton.isEnabled = currentIndex > 0
        binding.nextButton.isEnabled = currentIndex < urlMediaList.size - 1
    }

    private fun playNextMedia() {
        currentIndex++
        playMusic()
    }

    private fun playPreviousMedia() {
        currentIndex--
        playMusic()
    }

    private fun playOrPauseMedia() {
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