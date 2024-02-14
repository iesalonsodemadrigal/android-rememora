package com.iesam.rememora.app.presentation.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.ProgressBar
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.iesam.rememora.app.extensions.hide
import com.iesam.rememora.app.extensions.show
import com.iesam.rememora.databinding.ViewMediaplayerBinding

class MediaPlayerView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : FrameLayout(context, attrs) {

    private val binding = ViewMediaplayerBinding.inflate(LayoutInflater.from(context), this, true)
    private var urlMediaList: List<String> = mutableListOf()
    private var currentPosition = 0

    private var exoPlayer: ExoPlayer
    private var exoPlayerPrevious: ExoPlayer
    private var exoPlayerNext: ExoPlayer

    init {
        exoPlayer = ExoPlayer.Builder(context).build()
        exoPlayerPrevious = ExoPlayer.Builder(context).build()
        exoPlayerNext = ExoPlayer.Builder(context).build()
        binding.mediaView.player = exoPlayer
        binding.mediaPrevious.player = exoPlayerPrevious
        binding.mediaNext.player = exoPlayerNext
        setupView()
    }

    private fun setupView() {
        checkList()
        exoPlayer.addListener(object : Player.Listener {
            override fun onPlaybackStateChanged(state: Int) {
                when (state) {
                    Player.STATE_ENDED -> {
                        playMusic()
                        exoPlayer.pause()
                        exoPlayer.seekTo(0)
                    }
                }
            }
        })
        binding.apply {
            backButton.setOnClickListener {
                playPreviousMedia()
            }
            nextButton.setOnClickListener {
                playNextMedia()
            }
            pauseButton.setOnClickListener {
                pauseMedia()
            }
            playButton.setOnClickListener {
                playMedia()
            }
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

    private fun pauseMedia() {
        exoPlayer.let {
            it.pause()
        }
    }

    private fun playMedia() {
        exoPlayer.let {
            it.play()
        }
    }

    private fun playMusic() {
        checkList()
        bindMiniatures()
        if (currentPosition < urlMediaList.size) {
            val currentMusic = urlMediaList[currentPosition]
            val mediaItem = MediaItem.fromUri(currentMusic)
            exoPlayer.setMediaItem(mediaItem)
            exoPlayer.prepare()
            exoPlayer.play()
        }
    }

    private fun checkList() {
        binding.backButton.isEnabled = currentPosition > 0
        binding.nextButton.isEnabled = currentPosition < urlMediaList.size - 1
    }

    private fun bindMiniatures () {
        when (currentPosition) {
            0 -> {
                binding.mediaPrevious.hide()
                binding.mediaNext.show()
                exoPlayerNext.setMediaItem(MediaItem.fromUri(urlMediaList[currentPosition+1]))
                exoPlayerNext.prepare()
            }
            urlMediaList.size-1 -> {
                binding.mediaPrevious.show()
                binding.mediaNext.hide()
                exoPlayerPrevious.setMediaItem(MediaItem.fromUri(urlMediaList[currentPosition-1]))
                exoPlayerPrevious.prepare()
            }
            else -> {
                binding.mediaPrevious.show()
                binding.mediaNext.show()
                exoPlayerNext.setMediaItem(MediaItem.fromUri(urlMediaList[currentPosition+1]))
                exoPlayerPrevious.setMediaItem(MediaItem.fromUri(urlMediaList[currentPosition-1]))
                exoPlayerPrevious.prepare()
                exoPlayerNext.prepare()
            }
        }
    }

    fun render(mediaList: List<String>) {
        urlMediaList = mediaList
        playMusic()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        exoPlayer.release()
    }
}