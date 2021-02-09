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
package org.fuusio.flx.core.parser

class CharStream(expression: String) : ParserStream<Char> {

    private val source = expression.trim() + NUL
    private val maxPosition = source.length

    var position: Int = 0

    var tokenBeginPosition: Int = -1

    fun getTokenString(positionInclusive: Boolean = false): String {
        if (positionInclusive) position++
        return source.substring(tokenBeginPosition, --position)
    }

    override fun hasNext() = position < maxPosition

    override fun next() = source[position++]

    fun getTokenBeginChar(): Char {
        tokenBeginPosition = position
        return source[position++]
    }

    fun current(): Char = source[position]

    @Suppress("unused")
    companion object {
        const val AMBERSAND = '&'
        const val ASTERISK = '*'
        const val AT_SIGN = '@'
        const val BACKSLASH = '\\'
        const val CARET = '^'
        const val COLON = ':'
        const val COMMA = ','
        const val DOLLAR_SIGN = '$'
        const val DOUBLE_QUOTE = '"'
        const val EQUALS_TO = '='
        const val EXCLAMATION_MARK = '!'
        const val GREATER_THAN = '>'
        const val HYPHEN = '-'
        const val LEFT_BRACES = '{'
        const val LEFT_BRACKETS = '['
        const val LEFT_PARENTHESIS = '('
        const val LESSER_THAN = '<'
        const val LOWER_R = 'r'
        const val LOWER_X = 'x'
        const val NEW_LINE = '\n'
        const val NUL = '\u0000'
        const val NUMBER_SIGN = '#'
        const val PERIOD = '.'
        const val RIGHT_BRACES = '}'
        const val RIGHT_BRACKETS = ']'
        const val RIGHT_PARENTHESIS = ')'
        const val PERCENT_SIGN = '%'
        const val PLUS = '+'
        const val SEMI_COLON = ';'
        const val SLASH = '/'
        const val SPACE = ' '
        const val QUOTE = '\''
        const val TILDE = '~'
        const val UNDERSCORE = '_'
        const val UPPER_R = 'R'
        const val UPPER_X = 'X'
    }
}
