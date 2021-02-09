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
package com.flx.util

import android.content.Context
import android.graphics.Typeface
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import androidx.annotation.ColorRes
import com.flx.app.R
import java.lang.StringBuilder

class TextMarkupFormatter(val context: Context) {

    @ColorRes var boldSpanColor: Int = R.color.blue_grey_600
    @ColorRes var boldAndItalicSpanColor: Int = R.color.blue_grey_600
    @ColorRes var italicSpanColor: Int = R.color.blue_grey_600

    private val builder = SpannableStringBuilder()

    private var spanStartToken = NUL_TOKEN
    private var spanStartIndex = 0
    private var plainTextStarIndex = 0

    private lateinit var preprocessedString: String

    fun format(string: String): SpannableStringBuilder {
        preprocessedString = preProcess(string)

        val length = preprocessedString.length
        var index = 0

        while (index < length) {
            when (val token = preprocessedString[index]) {
                BOLD_START_TOKEN, BOLD_ITALIC_START_TOKEN, ITALIC_START_TOKEN -> {
                    require(spanStartToken == NUL_TOKEN)
                    spanStartToken = token
                    spanStartIndex = index + 1
                    if (index - plainTextStarIndex > 0)
                        builder.append(SpannableString(preprocessedString.substring(plainTextStarIndex, index)))
                }
                BOLD_END_TOKEN -> {
                    if (spanStartToken == BOLD_START_TOKEN) {
                        span(spanStartIndex, index, boldSpanColor, Typeface.BOLD)
                    }
                }
                BOLD_ITALIC_END_TOKEN -> {
                    if (spanStartToken == BOLD_ITALIC_START_TOKEN) {
                        span(spanStartIndex, index, boldAndItalicSpanColor, Typeface.BOLD_ITALIC)
                    }
                }
                ITALIC_END_TOKEN -> {
                    if (spanStartToken == ITALIC_START_TOKEN) {
                        span(spanStartIndex, index, italicSpanColor, Typeface.ITALIC)
                    }
                }
            }
            index++
        }

        if (index - plainTextStarIndex >= 0)
            builder.append(SpannableString(preprocessedString.substring(plainTextStarIndex, index)))

        return  builder
    }

    private fun preProcess(string: String): String {
        val builder = StringBuilder()

        val length = string.length
        var index = 0

        while (index < length) {
            when (val character = string[index]) {
                SKIP_TOKEN -> {
                    if (index + 1 < length) {
                        builder.append(string[index + 1])
                    } else {
                        builder.append(character)
                    }
                    index++
                }
                LEFT_SQUARE_BRACKET -> builder.append(BOLD_START_TOKEN)
                LEFT_CURLY_BRACKET -> builder.append(BOLD_ITALIC_START_TOKEN)
                LEFT_ROUND_BRACKET -> builder.append(ITALIC_START_TOKEN)
                RIGHT_SQUARE_BRACKET ->  builder.append(BOLD_END_TOKEN)
                RIGHT_CURLY_BRACKET -> builder.append(BOLD_ITALIC_END_TOKEN)
                RIGHT_ROUND_BRACKET -> builder.append(ITALIC_END_TOKEN)
                else -> builder.append(character)
            }
            index++
        }
        return builder.toString()
    }

    private fun span(start: Int, end: Int, @ColorRes foreground: Int, style: Int) {
        val spannableString = SpannableString(preprocessedString.substring(start, end))
        val spanEnd = end - start
        spannableString.setSpan(
            ForegroundColorSpan(context.getColor(foreground)), 0, spanEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        spannableString.setSpan(
            StyleSpan(style),0, spanEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        builder.append(spannableString)
        plainTextStarIndex = end + 1
        spanStartToken = NUL_TOKEN
    }

    companion object {

        const val NUL_TOKEN = '\u0000'

        const val SKIP_TOKEN = '^'

        const val LEFT_ROUND_BRACKET = '('
        const val RIGHT_ROUND_BRACKET = ')'

        const val LEFT_CURLY_BRACKET = '{'
        const val RIGHT_CURLY_BRACKET = '}'

        const val LEFT_SQUARE_BRACKET = '['
        const val RIGHT_SQUARE_BRACKET = ']'

        const val BOLD_START_TOKEN = '\uff3B'
        const val BOLD_END_TOKEN = '\uff3D'

        const val BOLD_ITALIC_START_TOKEN = '\uff5B'
        const val BOLD_ITALIC_END_TOKEN = '\uff5D'

        const val ITALIC_START_TOKEN = '\uff08'
        const val ITALIC_END_TOKEN = '\uff09'
    }
}