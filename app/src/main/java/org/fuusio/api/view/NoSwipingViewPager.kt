package org.fuusio.api.view

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.KeyEvent
import android.view.MotionEvent
import androidx.viewpager.widget.ViewPager

@Suppress("MemberVisibilityCanBePrivate")
class NoSwipingViewPager(context: Context, attributeSet: AttributeSet)
    : ViewPager(context, attributeSet) {

    var isSwipingEnabled = false

    override fun executeKeyEvent(event: KeyEvent) =
        if (isSwipingEnabled) super.executeKeyEvent(event) else false

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent) =
        if (isSwipingEnabled) super.onTouchEvent(event) else false

    override fun onInterceptTouchEvent(event: MotionEvent) =
        if (isSwipingEnabled) super.onInterceptTouchEvent(event) else false
}