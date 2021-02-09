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

import org.fuusio.flx.Flx
import org.fuusio.flx.core.Else
import org.fuusio.flx.core.Null
import org.fuusio.flx.core.parser.CharStream.Companion.DOUBLE_QUOTE
import org.fuusio.flx.core.parser.CharStream.Companion.HYPHEN
import org.fuusio.flx.core.parser.CharStream.Companion.LEFT_BRACES
import org.fuusio.flx.core.parser.CharStream.Companion.LEFT_BRACKETS
import org.fuusio.flx.core.parser.CharStream.Companion.LEFT_PARENTHESIS
import org.fuusio.flx.core.parser.CharStream.Companion.LOWER_R
import org.fuusio.flx.core.parser.CharStream.Companion.LOWER_X
import org.fuusio.flx.core.parser.CharStream.Companion.NEW_LINE
import org.fuusio.flx.core.parser.CharStream.Companion.NUL
import org.fuusio.flx.core.parser.CharStream.Companion.NUMBER_SIGN
import org.fuusio.flx.core.parser.CharStream.Companion.QUOTE
import org.fuusio.flx.core.parser.CharStream.Companion.RIGHT_BRACES
import org.fuusio.flx.core.parser.CharStream.Companion.RIGHT_BRACKETS
import org.fuusio.flx.core.parser.CharStream.Companion.RIGHT_PARENTHESIS
import org.fuusio.flx.core.parser.CharStream.Companion.SEMI_COLON
import org.fuusio.flx.core.parser.CharStream.Companion.SPACE
import org.fuusio.flx.core.parser.CharStream.Companion.UPPER_R
import org.fuusio.flx.core.parser.CharStream.Companion.UPPER_X
import java.util.*

sealed class Token {
    open fun content(): String = ""
    open fun toObject(): Any = Null
}

class ParsingCompletedToken : Token() {

    override fun content() = "END"

    companion object {
        val TOKEN = ParsingCompletedToken()

        fun begins(char: Char) = char == NUL
    }
}

class ArrayBeginToken : Token() {

    override fun content() = LEFT_BRACKETS.toString()

    override fun toObject() = Null

    companion object {
        val TOKEN = ArrayBeginToken()

        fun begins(char: Char) = char == LEFT_BRACKETS
    }
}

class ArrayEndToken : Token() {

    override fun content() = RIGHT_BRACKETS.toString()

    companion object {
        val TOKEN = ArrayEndToken()

        fun begins(char: Char) = char == RIGHT_BRACKETS
    }
}

class ListBeginToken : Token() {

    override fun content() = LEFT_PARENTHESIS.toString()

    companion object {
        val TOKEN = ListBeginToken()

        fun begins(char: Char) = char == LEFT_PARENTHESIS
    }
}

class ListEndToken : Token() {

    override fun content() = RIGHT_PARENTHESIS.toString()

    companion object {
        val TOKEN = ListEndToken()

        fun begins(char: Char) = char == RIGHT_PARENTHESIS
    }
}

class MapBeginToken : Token() {

    override fun content() = LEFT_BRACES.toString()

    companion object {
        val TOKEN = MapBeginToken()

        fun begins(char: Char) = char == LEFT_BRACES
    }
}

class MapOrSetEndToken : Token() {

    override fun content() = RIGHT_BRACES.toString()

    companion object {
        val TOKEN = MapOrSetEndToken()

        fun begins(char: Char) = char == RIGHT_BRACES
    }
}

//class QuoteToken : Token() {
//
//    override fun content() = QUOTE.toString()
//
//    companion object {
//        val TOKEN = QuoteToken()
//
//        fun begins(char: Char) = char == QUOTE
//    }
//}

class SetBeginToken : Token() {

    override fun content() = "#{"

    companion object {
        val TOKEN = SetBeginToken()
    }
}

class CharToken(val value: Char) : Token() {

    override fun content() = value.toString()

    override fun toObject(): Any = value

    companion object {
        fun begins(char: Char) = char == QUOTE

        fun parse(stream: CharStream): Token {
            while(stream.hasNext()) {
                val char = stream.next()
                when {
                    TokenChar.isCharEndChar(char) -> return createToken(stream)
                    else -> {}
                }
            }
            return ErrorToken(ParserErrorCode.FAILED_TO_PARSE_CHAR, stream.position)
        }

        private fun createToken(stream: CharStream): Token {
            val tokenString = stream.getTokenString(true)
            val char = parseChar(tokenString)
            return if (char != null) CharToken(char) else ErrorToken(ParserErrorCode.INVALID_CHAR, tokenString)
        }

        fun parseChar(tokenString: String): Char? {
            var isValidToken = true
            val string = tokenString.substring(1, tokenString.length - 1)
            val char = when {
                string.length == 1 -> string[0]
                string.length == 2 -> string[1]
                string.length == 6 && string.startsWith("\\u") -> {
                    val unicode = string.substring(2)
                    try {
                        unicode.toInt(16).toChar()
                    } catch (e: Exception) {
                        isValidToken = false
                        NUL
                    }
                }
                else -> {
                    isValidToken = false
                    NUL
                }
            }
            return if (isValidToken) char else null
        }
    }
}

class BaseIntegerToken(val value: Number) : Token() {

    override fun content() = value.toString()

    override fun toObject(): Any = value

    companion object {

        fun parse(stream: CharStream): Token {
            while(stream.hasNext()) {
                val char = stream.next()
                when {
                    char.isLetterOrDigit() -> {}
                    char == 'B' -> return createToken(stream, true)
                    char == 'L' -> return createToken(stream, true)
                    TokenChar.isNumberEndChar(char) -> return createToken(stream)
                    else -> ErrorToken(ParserErrorCode.FAILED_TO_PARSE_BASE_INTEGER, stream.position)
                }
            }
            return ErrorToken(ParserErrorCode.FAILED_TO_PARSE_BASE_INTEGER, stream.position)
        }

        private fun createToken(stream: CharStream, positionInclusive: Boolean = false): Token {
            val tokenString = stream.getTokenString(positionInclusive)
            val normalizedTokenString = tokenString.toLowerCase(Locale.US)
            val index = normalizedTokenString.indexOf('r')

            return try {
                val base = tokenString.substring(0, index).toInt()
                val valueString = tokenString.substring(index + 1)
                when (valueString.last()) {
                    'B' -> BaseIntegerToken(valueString.substring(0, valueString.length - 1).toByte(base))
                    'L' -> BaseIntegerToken(valueString.substring(0, valueString.length - 1).toLong(base))
                    else -> BaseIntegerToken(valueString.toInt(base))
                }
            } catch (e: Exception) {
                ErrorToken(ParserErrorCode.INVALID_BASE_INTEGER, tokenString)
            }
        }
    }
}

class IntegerToken(val value: Number) : Token() {

    override fun content() = value.toString()

    override fun toObject(): Any = value

    companion object {
        fun begins(char: Char) = char.isDigit() || char == HYPHEN

        fun parse(stream: CharStream): Token {
            while(stream.hasNext()) {
                val previousChar = stream.current()
                val char = stream.next()
                when {
                    previousChar == '-'  ->  return SymbolToken.parse(stream)
                    char.isDigit() -> {}
                    char == '.' -> return DoubleToken.parse(stream)
                    char == LOWER_R || char == UPPER_R -> return BaseIntegerToken.parse(stream)
                    char == LOWER_X || char == UPPER_X -> return HexadecimalToken.parse(stream)
                    char == 'B' -> return createToken(stream, true)
                    char == 'L' -> return createToken(stream, true)
                    TokenChar.isNumberEndChar(char) -> return createToken(stream)
                    else -> return ErrorToken(ParserErrorCode.FAILED_TO_PARSE_LONG, stream.position)
                }
            }
            return ErrorToken(ParserErrorCode.FAILED_TO_PARSE_LONG, stream.position)
        }

        private fun createToken(stream: CharStream, positionInclusive: Boolean = false): Token {
            val tokenString = stream.getTokenString(positionInclusive)
            return if (tokenString == "-") {
                SymbolToken(tokenString)
            } else {
                try {
                    when (tokenString.last()) {
                        'B' -> IntegerToken(tokenString.substring(0, tokenString.length - 1).toByte())
                        'L' -> IntegerToken(tokenString.substring(0, tokenString.length - 1).toLong())
                        else -> IntegerToken(tokenString.toInt())
                    }
                } catch (e: Exception) {
                    ErrorToken(ParserErrorCode.INVALID_LONG, tokenString)
                }
            }
        }
    }
}

class HexadecimalToken(val value: Number) : Token() {

    override fun content() = value.toString()

    override fun toObject(): Any = value

    companion object {

        fun parse(stream: CharStream): Token {
            while(stream.hasNext()) {
                val char = stream.next()
                when {
                    char.isLetterOrDigit() -> {}
                    char == 'L' -> return createToken(stream, true)
                    TokenChar.isNumberEndChar(char) -> return createToken(stream)
                    else -> return ErrorToken(ParserErrorCode.FAILED_TO_PARSE_HEXADECIMAL, stream.position)
                }
            }
            return ErrorToken(ParserErrorCode.FAILED_TO_PARSE_HEXADECIMAL, stream.position)
        }

        private fun createToken(stream: CharStream, positionInclusive: Boolean = false): Token {
            val tokenString = stream.getTokenString(positionInclusive)
            return try {
                when (tokenString.last()) {
                    'L' -> HexadecimalToken(tokenString.substring(2, tokenString.length - 1).toLong(16))
                    else -> HexadecimalToken(tokenString.substring(2).toInt(16))
                }
            } catch (e: Exception) {
                ErrorToken(ParserErrorCode.INVALID_HEXADECIMAL, tokenString)
            }
        }
    }
}

class DoubleToken(val value: Double) : Token() {

    override fun content() = value.toString()

    override fun toObject(): Any = value

    companion object {
        fun parse(stream: CharStream): Token {
            while(stream.hasNext()) {
                val char = stream.next()
                when {
                    char.isDigit() -> {}
                    TokenChar.isNumberEndChar(char) -> return createToken(stream)
                    else -> ErrorToken(ParserErrorCode.FAILED_TO_PARSE_DOUBLE, stream.position)
                }
            }
            return ErrorToken(ParserErrorCode.FAILED_TO_PARSE_DOUBLE, stream.position)
        }

        private fun createToken(stream: CharStream): Token {
            val tokenString = stream.getTokenString()
            return try {
                DoubleToken(tokenString.toDouble())
            } catch (e: Exception) {
                ErrorToken(ParserErrorCode.INVALID_DOUBLE, tokenString)
            }
        }
    }
}

class KeywordToken(val string: String) : Token() {

    override fun content() = string

    override fun toObject(): Any = Flx.getKeyword(string.replace(":", ""))

    companion object {

        fun begins(char: Char) = TokenChar.isKeywordBeginChar(char)

        fun parse(stream: CharStream): Token {
            while(stream.hasNext()) {
                val char = stream.next()

                when {
                    TokenChar.isKeywordChar(char) -> {}
                    TokenChar.isKeywordEndChar(char) -> return KeywordToken(stream.getTokenString())
                    else -> return ErrorToken(ParserErrorCode.FAILED_TO_PARSE_KEYWORD, stream.position)
                }
            }
            return ErrorToken(ParserErrorCode.FAILED_TO_PARSE_KEYWORD, stream.position)
        }
    }
}

class RegexToken(val string: String) : Token() {

    override fun content() = string

    override fun toObject(): Regex = string.toRegex()

    companion object {

        fun begins(char: Char) = char == NUMBER_SIGN

        fun parse(stream: CharStream): Token {
            while(stream.hasNext()) {
                return when (stream.next()) {
                    LEFT_BRACES -> SetBeginToken.TOKEN
                    DOUBLE_QUOTE -> parseRegex(stream)
                    NUMBER_SIGN -> SymbolToken.parse(stream)
                    else -> ErrorToken(ParserErrorCode.FAILED_TO_PARSE_REGEX_OR_SET, stream.position)
                }
            }
            return ErrorToken(ParserErrorCode.FAILED_TO_PARSE_REGEX, stream.position)
        }

        private fun parseRegex(stream: CharStream): Token {
            while(stream.hasNext()) {
                val char = stream.next()
                when {
                    char == DOUBLE_QUOTE -> {
                        val tokenString = stream.getTokenString(true)
                        return RegexToken(tokenString.substring(2, tokenString.length - 1))
                    }

                    TokenChar.isRegexEndChar(char) -> return ErrorToken(ParserErrorCode.FAILED_TO_PARSE_REGEX, stream.position)
                    else -> {}
                }
            }
            return ErrorToken(ParserErrorCode.FAILED_TO_PARSE_REGEX, stream.position)
        }
    }
}

class StringToken(val string: String) : Token() {

    override fun content() = "\"$string\""

    override fun toObject(): Any = string

    companion object {

        fun begins(char: Char) = char == DOUBLE_QUOTE

        fun parse(stream: CharStream): Token {
            while(stream.hasNext()) {
                when(stream.next()) {
                    DOUBLE_QUOTE -> {
                        val tokenString = stream.getTokenString(true)
                        return StringToken(tokenString.substring(1, tokenString.length - 1))
                    }
                    else -> {}
                }
            }
            return ErrorToken(ParserErrorCode.FAILED_TO_PARSE_STRING, stream.position)
        }
    }
}

class SymbolToken(val string: String) : Token() {

    override fun content() = string

    override fun toObject(): Any =
        when (string) {
            SYMBOL_E -> Math.E
            SYMBOL_FALSE -> false
            SYMBOL_NIL -> Null
            SYMBOL_NULL -> Null
            SYMBOL_PI -> Math.PI
            SYMBOL_TRUE -> true
            SYMBOL_ELSE -> Else
            else -> Flx.getSymbol(string)
        }

    companion object {

        const val SYMBOL_E = "e"
        const val SYMBOL_FALSE = "false"
        const val SYMBOL_NIL = "nil"
        const val SYMBOL_NULL = "null"
        const val SYMBOL_PI = "pi"
        const val SYMBOL_TRUE = "true"
        const val SYMBOL_ELSE = "_*_else"

        fun begins(char: Char) = TokenChar.isSymbolBeginChar(char)

        fun parse(stream: CharStream): Token {
            while(stream.hasNext()) {
                val char = stream.next()
                when {
                    TokenChar.isSymbolChar(char) -> {}
                    TokenChar.isSymbolEndChar(char) -> return SymbolToken(stream.getTokenString())
                    else -> return ErrorToken(ParserErrorCode.FAILED_TO_PARSE_SYMBOL, stream.position)
                }
            }
            return ErrorToken(ParserErrorCode.FAILED_TO_PARSE_SYMBOL, stream.position)
        }
    }
}

class ErrorToken(code: ParserErrorCode, vararg args: Any) : Token() {

    val error = ParserError.create(code, *args)

    override fun content() = error.description
}

class CommentToken(val string: String) : Token() {

    override fun content() = string

    companion object {
        fun begins(char: Char) = char == SEMI_COLON

        fun parse(stream: CharStream): Token {
            while(stream.hasNext()) {
                when(stream.next()) {
                    NEW_LINE , NUL -> return createCommentToken(stream)
                    else -> {}
                }
            }
            return createCommentToken(stream)
        }

        private fun createCommentToken(stream: CharStream): Token {
            val tokenString = stream.getTokenString(true)
            return if (tokenString.startsWith(";;")) {
                val lastChar = tokenString.last()
                if (lastChar == NEW_LINE || lastChar == NUL) CommentToken(tokenString.substring(0, tokenString.length - 1)) else CommentToken(tokenString)
            } else ErrorToken(ParserErrorCode.FAILED_TO_PARSE_COMMENT, stream.position)
        }
    }
}

class WhiteSpaceToken(val string: String) : Token() {

    override fun content(): String {
        var content = ""

        string.forEach { char ->
            content += if (char == SPACE) '_' else '$'
        }
        return content
    }

    companion object {
        fun begins(char: Char) = char.isWhitespace()

        fun parse(stream: CharStream): Token {
            while(stream.hasNext()) {
                val char = stream.next()
                when {
                    char.isWhitespace() || char == NUL -> {}
                    else -> return WhiteSpaceToken(stream.getTokenString())
                }
            }
            throw IllegalStateException()
        }
    }
}