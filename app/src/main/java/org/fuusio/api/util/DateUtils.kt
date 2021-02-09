/*
 * Copyright (C) 2001 - 2021 Marko Salmela
 *
 * http://fuusio.org
 *
 * All rights reserved.
 */
package org.fuusio.api.util

import java.text.SimpleDateFormat
import java.util.*

object DateUtils {

    const val PATTERN_yyyy_MM_dd = "yyyy-MM-dd"
    const val PATTERN_yyyy_MM_dd_HH_mm = "yyyy-MM-dd HH:mm"
    const val PATTERN_yyyy_MM_dd_HH_mm_ss = "yyyy-MM-dd HH:mm:ss"

    fun format(date: Date, pattern: String = PATTERN_yyyy_MM_dd_HH_mm): String {
        val format = SimpleDateFormat(pattern)
        return format.format(date)
    }
}