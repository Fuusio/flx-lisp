package org.fuusio.api.extension

import android.animation.Animator
import android.view.View

fun View.fadeIn(duration: Long, listener: Animator.AnimatorListener? = null) {
    this.apply {
        alpha = 0f
        visibility = View.VISIBLE
        animate()
            .alpha(1f)
            .setDuration(duration)
            .setListener(listener)
    }
}

fun View.fadeOut(duration: Long, listener: Animator.AnimatorListener? = null) {
    this.apply {
        alpha = 1f
        visibility = View.VISIBLE
        animate()
            .alpha(0f)
            .setDuration(duration)
            .setListener(listener)
    }
}