package org.fuusio.api.view

import android.graphics.drawable.Drawable
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.fragment.app.Fragment
import org.koin.core.KoinComponent

abstract class AbstractBottomSheet<T: Fragment>(val fragment: T) : KoinComponent {

    protected val activity = fragment.requireActivity()
    protected val context = fragment.requireContext()
    protected val resources = fragment.resources

    @ColorInt
    protected fun getColor(@ColorRes color: Int): Int = resources.getColor(color, context.theme)

    protected fun getDrawable(@DrawableRes drawableRes: Int): Drawable =
        ResourcesCompat.getDrawable(resources, drawableRes, context.theme)!!

    protected fun getString(@StringRes stringRes: Int): String = resources.getString(stringRes)

    fun tintDrawable(drawable: Drawable, color: Int): Drawable {
        val wrapped = DrawableCompat.wrap(drawable)
        drawable.mutate()
        DrawableCompat.setTint(wrapped, color)
        return drawable
    }
}