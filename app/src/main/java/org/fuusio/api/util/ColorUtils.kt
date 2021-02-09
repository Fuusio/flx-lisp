package org.fuusio.api.util

import android.graphics.Color
import androidx.annotation.ColorInt

object ColorUtils {

    fun parseColor(colorString: String): Int = Color.parseColor(colorString)

    fun toString(@ColorInt color: Int, alwaysIncludeAlpha: Boolean = false): String {
        val alphaValue = (0xff and Color.alpha(color))
        val red = String.format("%02X", (0xff and Color.red(color)))
        val green = String.format("%02X", (0xff and Color.green(color)))
        val blue = String.format("%02X", (0xff and Color.blue(color)))
        val alpha = String.format("%02X", alphaValue)

        return when {
            alwaysIncludeAlpha -> "#$alpha$red$green$blue"
            alphaValue < 0xff -> "#$alpha$red$green$blue"
            else -> "#$red$green$blue"
        }
    }

    @ColorInt fun toOpaqueArgb(@ColorInt color: Int): Int {
        val red = Color.red(color)
        val green = Color.green(color)
        val blue = Color.blue(color)
        return Color.argb(0xff, red, green, blue)
    }
}