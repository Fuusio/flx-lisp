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

@Suppress("unused")
enum class TokenChar(
    val char: Char,
    val isSymbolBeginChar: Boolean = true,
    val isSymbolChar: Boolean = true,
    val isTokenEndChar: Boolean = true
) {
    AMBERSAND('&', isSymbolBeginChar = false, isSymbolChar = false),
    ASTERISK('*'),
    AT_SIGN('@', isSymbolBeginChar = false, isSymbolChar = false),
    BACKSLASH('\\', isSymbolBeginChar = false, isSymbolChar = false),
    CARET('^', isSymbolBeginChar = false, isSymbolChar = false),
    COLON(':', isSymbolBeginChar = false, isSymbolChar = false),
    COMMA(',', isSymbolBeginChar = false, isSymbolChar = false),
    DOLLAR_SIGN('$', isSymbolBeginChar = false, isSymbolChar = false),
    DOUBLE_QUOTE('"', isSymbolBeginChar = false, isSymbolChar = false),
    EQUALS_TO('='),
    EXCLAMATION_MARK('!'),
    GREATER_THAN('>'),
    HYPHEN('-'),
    LEFT_BRACES('{', isSymbolBeginChar = false, isSymbolChar = false),
    LEFT_BRACKETS('[', isSymbolBeginChar = false, isSymbolChar = false),
    LEFT_PARENTHESIS('(', isSymbolBeginChar = false, isSymbolChar = false),
    LESSER_THAN('<'),
    NEW_LINE('\n', isSymbolBeginChar = false, isSymbolChar = false, isTokenEndChar = true),
    NUL('\u0000', isSymbolBeginChar = false, isSymbolChar = false, isTokenEndChar = true),
    NUMBER_SIGN('#', isSymbolBeginChar = false, isSymbolChar = false),
    PERIOD('.'),
    RIGHT_BRACES('}', isSymbolBeginChar = false, isSymbolChar = false, isTokenEndChar = true),
    RIGHT_BRACKETS(']', isSymbolBeginChar = false, isSymbolChar = false, isTokenEndChar = true),
    RIGHT_PARENTHESIS(')', isSymbolBeginChar = false, isSymbolChar = false, isTokenEndChar = true),
    PERCENT_SIGN('%', isSymbolChar = false),
    PLUS('+'),
    QUESTION_MARK('?'),
    SEMI_COLON(';', isSymbolBeginChar = false, isSymbolChar = false),
    SLASH('/', isSymbolChar = false),
    SPACE(' ', isSymbolBeginChar = false, isSymbolChar = false, isTokenEndChar = true),
    QUOTE('\'', isSymbolBeginChar = false, isSymbolChar = false),
    TILDE('~', isSymbolBeginChar = false, isSymbolChar = false),
    UNDERSCORE('_');

    fun matches(char: Char) = this.char == char

    companion object {

        private val mapping = hashMapOf<Char, TokenChar>()

        init {
            values().forEach { value -> mapping[value.char] = value }
        }

        fun get(char: Char) = mapping[char] ?: NUL

        fun isCharEndChar(char: Char) = char == QUOTE.char

        fun isNumberBeginChar(char: Char) = char.isDigit() || HYPHEN.matches(char)

        fun isNumberEndChar(char: Char) = char.isWhitespace() || get(char).isTokenEndChar

        fun isIntegerChar(char: Char) = char.isDigit()

        fun isKeywordBeginChar(char: Char) = COLON.matches(char)

        fun isKeywordChar(char: Char) =
            char != NUL.char && (char.isJavaIdentifierPart() || get(char).isSymbolChar)

        fun isKeywordEndChar(char: Char) = char.isWhitespace() || get(char).isTokenEndChar

        fun isRegexEndChar(char: Char) = NUL.matches(char)

        fun isSymbolBeginChar(char: Char) =
            char != NUL.char && (char.isJavaIdentifierStart() || get(char).isSymbolBeginChar)

        fun isSymbolChar(char: Char) =
            char != NUL.char && (char.isJavaIdentifierPart() || get(char).isSymbolChar)

        fun isSymbolEndChar(char: Char) = char.isWhitespace() || get(char).isTokenEndChar
    }
}