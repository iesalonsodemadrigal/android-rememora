package com.iesam.rememora.app.presentation.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.iesam.rememora.R
import com.iesam.rememora.databinding.ViewMediaplayerBinding

class MediaPlayerView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : FrameLayout(context, attrs) {

    private val binding = ViewMediaplayerBinding.inflate(LayoutInflater.from(context), this, true)
    private var urlMediaList: List<String> = mutableListOf()
    private var currentPosition = 0
    private var label: String = ""

    private var exoPlayer: ExoPlayer

    init {
        exoPlayer = ExoPlayer.Builder(context).build()
        binding.mediaView.player = exoPlayer
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
            menuButton.setOnClickListener {
                playerButtonsContainer.visibility = VISIBLE
                menuButtonAction.visibility = GONE
            }
            backButton.setOnClickListener {
                playPreviousMedia()
                backToMenu()
            }
            nextButton.setOnClickListener {
                playNextMedia()
                backToMenu()
            }
            pauseButton.setOnClickListener {
                pauseMedia()
                backToMenu()
            }
            playButton.setOnClickListener {
                playMedia()
                backToMenu()
            }
        }
    }

    private fun backToMenu() {
        binding.apply {
            playerButtonsContainer.visibility = GONE
            menuButtonAction.visibility = VISIBLE
        }

    }

    private fun playNextMedia() {
        currentPosition++
        playMusic()
        pauseMedia()
    }

    private fun playPreviousMedia() {
        currentPosition--
        playMusic()
        pauseMedia()
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
        bindLabelNum()
        checkList()
        if (currentPosition < urlMediaList.size) {
            val currentMusic = urlMediaList[currentPosition]
            val mediaItem = MediaItem.fromUri(currentMusic)
            exoPlayer.setMediaItem(mediaItem)
            exoPlayer.prepare()
            exoPlayer.play()
        }
    }

    private fun bindLabelNum() {
        binding.labelNum.text = context.getString(
            R.string.label_navigation,
            (currentPosition + 1).toString(),
            urlMediaList.size.toString(),
            label
        )
    }

    private fun checkList() {
        binding.backButton.isEnabled = currentPosition > 0
        binding.nextButton.isEnabled = currentPosition < urlMediaList.size - 1
    }

    fun render(mediaList: List<String>, label: String) {
        urlMediaList = mediaList
        this.label = label
        playMusic()
        pauseMedia()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        exoPlayer.release()
    }
}