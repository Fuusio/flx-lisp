package org.fuusio.api.animation

import android.view.View
import com.google.android.material.floatingactionbutton.FloatingActionButton

object ViewAnimation {

    const val SHORT_ANIMATION_DURATION = 100L

    private const val DEFAULT_ANIMATION_DURATION = 200L

    fun rotateView(view: View, rotate: Boolean): Boolean {
        view.animate()
            .setDuration(DEFAULT_ANIMATION_DURATION)
            .rotation(if (rotate) 135f else 0f)
        return rotate
    }

    fun showFab(fab: FloatingActionButton, translation: Float) {
        val translationY = fab.context.resources.displayMetrics.density * translation
        fab.visibility = View.VISIBLE
        fab.alpha = 0f
        fab.translationY = 0f
        fab.animate()
            .setDuration(DEFAULT_ANIMATION_DURATION)
            .translationY(translationY)
            .alpha(1f)
            .start()
    }

    fun hideFab(fab: FloatingActionButton) {
        fab.animate()
            .setDuration(DEFAULT_ANIMATION_DURATION)
            .translationY(0f)
            .alpha(0f)
            .start()
    }

    fun initFab(fab: FloatingActionButton) {
        fab.visibility = View.GONE
        fab.translationY = fab.height.toFloat()
        fab.alpha = 0f
    }
}