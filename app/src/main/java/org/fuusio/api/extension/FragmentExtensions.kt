package org.fuusio.api.extension

import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment

@ColorInt fun Fragment.getColor(@ColorRes colorRes: Int): Int {
    val context = requireContext()
    val resources = context.resources
    return resources.getColor(colorRes, context.theme)
}