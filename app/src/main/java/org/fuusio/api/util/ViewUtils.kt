/*
 * Copyright (C) 2001 - 2020 Marko Salmela
 *
 * http://fuusio.org
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.fuusio.api.util

import android.content.Context
import android.util.DisplayMetrics
import android.view.View

object ViewUtils {

    fun dpToPx(context: Context, dp: Float): Float {
        return dp * context.resources.displayMetrics.density
    }

    fun pxToDp(px: Float, context: Context): Float {
        val metrics = context.resources.displayMetrics
        return px / (metrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
    }

    fun hitTest(view: View, x: Int, y: Int): Boolean {
        val tx = (view.translationX + 0.5f).toInt()
        val ty = (view.translationY + 0.5f).toInt()
        val left: Int = view.left + tx
        val right: Int = view.right + tx
        val top: Int = view.top + ty
        val bottom: Int = view.bottom + ty
        return x in left..right && y in top..bottom
    }
}