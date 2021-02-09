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

class TokenStream(expression: String) : ParserStream<Token> {

    private val charStream = CharStream(expression)

    override fun hasNext() = charStream.hasNext()

    override fun next(): Token {
        val char = charStream.getTokenBeginChar()
        return when {
            ArrayBeginToken.begins(char) -> ArrayBeginToken.TOKEN
            ArrayEndToken.begins(char) -> ArrayEndToken.TOKEN
            ListBeginToken.begins(char) -> ListBeginToken.TOKEN
            ListEndToken.begins(char) -> ListEndToken.TOKEN
            MapBeginToken.begins(char) -> MapBeginToken.TOKEN
            MapOrSetEndToken.begins(char) -> MapOrSetEndToken.TOKEN
            // QuoteToken.begins(char) -> QuoteToken.TOKEN
            ParsingCompletedToken.begins(char) -> ParsingCompletedToken.TOKEN

            CharToken.begins(char) -> CharToken.parse(charStream)
            CommentToken.begins(char) -> CommentToken.parse(charStream)
            IntegerToken.begins(char) -> IntegerToken.parse(charStream)
            KeywordToken.begins(char) -> KeywordToken.parse(charStream)
            ParsingCompletedToken.begins(char) -> ParsingCompletedToken.TOKEN
            RegexToken.begins(char) -> RegexToken.parse(charStream)
            StringToken.begins(char) -> StringToken.parse(charStream)
            SymbolToken.begins(char) -> SymbolToken.parse(charStream)
            WhiteSpaceToken.begins(char) -> WhiteSpaceToken.parse(charStream)

            else -> ErrorToken(ParserErrorCode.UNKNOWN_CHAR, char.toString())
        }
    }

    fun getPosition() = charStream.position

    fun getTokenBeginPosition() = charStream.tokenBeginPosition
}
