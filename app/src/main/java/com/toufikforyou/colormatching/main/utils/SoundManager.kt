package com.toufikforyou.colormatching.main.utils

import android.content.Context
import android.media.MediaPlayer
import com.toufikforyou.colormatching.R

class SoundManager(private val context: Context) {
    private var buttonClickPlayer: MediaPlayer? = null
    private var matchFoundPlayer: MediaPlayer? = null
    private var levelCompletePlayer: MediaPlayer? = null

    fun playButtonClick() {
        buttonClickPlayer?.let {
            if (!it.isPlaying) {
                it.start()
            }
        } ?: run {
            buttonClickPlayer = MediaPlayer.create(context, R.raw.tabsound)
            buttonClickPlayer?.start()
        }
    }

    fun playMatchFound() {
        matchFoundPlayer?.let {
            if (!it.isPlaying) {
                it.start()
            }
        } ?: run {
            matchFoundPlayer = MediaPlayer.create(context, R.raw.tabsound)
            matchFoundPlayer?.start()
        }
    }

    fun playLevelComplete() {
        levelCompletePlayer?.let {
            if (!it.isPlaying) {
                it.start()
            }
        } ?: run {
            levelCompletePlayer = MediaPlayer.create(context, R.raw.matchsound)
            levelCompletePlayer?.start()
        }
    }

    fun release() {
        buttonClickPlayer?.release()
        matchFoundPlayer?.release()
        levelCompletePlayer?.release()
        buttonClickPlayer = null
        matchFoundPlayer = null
        levelCompletePlayer = null
    }
} 