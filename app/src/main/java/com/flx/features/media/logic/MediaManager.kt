package com.flx.features.media.logic

import android.content.Context
import android.media.MediaPlayer
import android.net.Uri
import org.fuusio.api.feature.FeatureManager

class MediaManager(private val appContext: Context) : FeatureManager() {

    fun playAudio(audioUri: Uri): MediaPlayer {
        val mediaPlayer = MediaPlayer.create(appContext, audioUri)
        mediaPlayer.start()
        return mediaPlayer
    }
}