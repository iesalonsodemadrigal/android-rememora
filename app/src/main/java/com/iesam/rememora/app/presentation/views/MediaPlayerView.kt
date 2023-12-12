package com.iesam.rememora.app.presentation.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import com.iesam.rememora.R
import com.iesam.rememora.databinding.ViewMediaBinding

class MediaPlayerView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : FrameLayout(context, attrs) {

    private val binding = ViewMediaBinding.inflate(LayoutInflater.from(context), this, true)
    private var urlMediaList: List<String> = mutableListOf()
    private var currentPosition = 0

    private var exoPlayer: ExoPlayer

    init {
        exoPlayer = ExoPlayer.Builder(context).build()
        setupView()
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
        binding.backButton.isEnabled = currentPosition > 0
        binding.nextButton.isEnabled = currentPosition < urlMediaList.size - 1
    }

    fun render(mediaList: List<String>, playerView: PlayerView) {
        urlMediaList = mediaList
        playerView.player = exoPlayer
        playMusic()


    }


    private fun playMusic() {
        checkList()
        if (currentPosition < urlMediaList.size) {
            val currentMusic = urlMediaList[currentPosition]
            val mediaItem = MediaItem.fromUri(currentMusic)
            exoPlayer.setMediaItem(mediaItem)
            exoPlayer.prepare()
            exoPlayer.play()


        }
    }


    private fun playNextMedia() {
        currentPosition++
        playMusic()
    }

    private fun playPreviousMedia() {
        currentPosition--
        playMusic()
    }

    private fun playOrPauseMedia() {
        exoPlayer.let {
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