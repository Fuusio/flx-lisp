/*
 * Copyright (C) 2016 - 2021 Marko Salmela
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
import android.content.res.TypedArray
import android.graphics.Color
import android.os.Build
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt
import androidx.appcompat.app.AppCompatDelegate


object ThemeUtils {
    internal const val LIGHT_MODE = "light"
    internal const val DARK_MODE = "dark"
    internal const val DEFAULT_MODE = "default"

    fun applyDefaultTheme() {
        applyTheme(DEFAULT_MODE)
    }

    fun applyLightTheme() {
        applyTheme(LIGHT_MODE)
    }

    fun applyDarkTheme() {
        applyTheme(DARK_MODE)
    }

    internal fun applyTheme(theme: String) {
        when (theme) {
            LIGHT_MODE -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            DARK_MODE -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            else -> AppCompatDelegate.setDefaultNightMode(getDefaultNightMode())
        }
    }

    private fun getDefaultNightMode() =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
            AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
        else
            AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY

    /**
     * Queries the theme of the given [context] for a theme color returned as [Int].
     * Parameter [attrResId] specifies the theme color attribute to resolve.
     */
    @ColorInt
    fun getThemeColor(context: Context, @AttrRes attrResId: Int): Int {
        val typedArray: TypedArray = context.obtainStyledAttributes(null, intArrayOf(attrResId))
        return try {
            typedArray.getColor(0, Color.MAGENTA)
        } finally {
            typedArray.recycle()
        }
    }
}