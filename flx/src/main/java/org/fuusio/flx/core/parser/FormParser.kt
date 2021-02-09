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

import org.fuusio.flx.core.type.impl.ImmutableArray
import org.fuusio.flx.core.type.impl.ImmutableList
import org.fuusio.flx.core.type.impl.ImmutableMap
import org.fuusio.flx.core.type.impl.ImmutableSet

class FormParser(private val observer: ParserObserver) {

    fun parse(context: ParsingContext, expression: String): List<Any> {
        val parsingResults = mutableListOf<Any>()
        context.tokenStream = TokenStream(expression)

        while (context.canContinue()) {
            parse(context, parsingResults)?.let { result ->
                parsingResults.add(result)
                observer.onNext(result)
            }
        }
        if (context.hasErrors()) parsingResults.clear()
        return parsingResults
    }

    private fun parse(
        context: ParsingContext,
        elements: MutableList<Any>,
        beginToken: Token? = null,
        endToken: Token? = null
    ): Any? {

        // var isQuoted = false

        while (context.canContinue()) {
            val token = context.nextToken()
            val result: Any? = when (token) {

                is SymbolToken -> token.toObject()
                is StringToken -> token.toObject()
                is IntegerToken -> token.toObject()
                is DoubleToken -> token.toObject()
                is CharToken -> token.toObject()

                is ListBeginToken -> parseStructure(context, token, ListEndToken.TOKEN)
                is ListEndToken -> createList(context, elements)

                is ArrayBeginToken -> parseStructure(context, token, ArrayEndToken.TOKEN)
                is ArrayEndToken -> createArray(context, elements)

                is MapBeginToken -> parseStructure(context, token, MapOrSetEndToken.TOKEN)
                is MapOrSetEndToken -> createMapOrSet(context, beginToken!!, elements)

                is SetBeginToken -> parseStructure(context, token, MapOrSetEndToken.TOKEN)

                is BaseIntegerToken -> token.toObject()
                is HexadecimalToken -> token.toObject()
                is KeywordToken -> token.toObject()
                is RegexToken -> token.toObject()

//                is QuoteToken -> {
//                    isQuoted = true
//                    null
//                }

                is CommentToken -> null
                is WhiteSpaceToken -> null
                is ErrorToken -> {
                    onError(context, ParserErrorCode.FAILED_TO_A_FORM, context.tokenStream.getTokenBeginPosition())
                    null
                }

                is ParsingCompletedToken -> {
                    if (endToken != null) {
                        val code = when(beginToken) {
                            ArrayBeginToken.TOKEN -> ParserErrorCode.FAILED_TO_PARSE_ARRAY
                            ListBeginToken.TOKEN -> ParserErrorCode.FAILED_TO_PARSE_LIST
                            MapBeginToken.TOKEN -> ParserErrorCode.FAILED_TO_PARSE_MAP
                            SetBeginToken.TOKEN -> ParserErrorCode.FAILED_TO_PARSE_SET
                            else -> ParserErrorCode.UNKNOWN_ERROR
                        }
                        onError(context, code, context.tokenStream.getPosition())
                    }
                    context.parsingCompleted = true
                    null
                }
            }

//            if (isQuoted && token !is QuoteToken) {
//                isQuoted = false
//                result?.let {
//                    result = createQuote(it)
//                }
//            }

            when {
                token == endToken -> {
                    context.decParsingLevel()
                    return result
                }
                // context.isTopParsingLevel() && !isQuoted -> return result
                context.isTopParsingLevel() -> return result
                else -> result?.let { elements.add(it) }
            }
        }
        return null
    }

    private fun parseStructure(context: ParsingContext, beginToken: Token, endToken: Token): Any? {
        context.incParsingLevel()
        return parse(context, mutableListOf(), beginToken, endToken)
    }

    private fun createArray(context: ParsingContext, elements: MutableList<Any>): Any? {
        return if (context.token == ArrayEndToken.TOKEN)
            ImmutableArray(elements)
        else
            onError(context, ParserErrorCode.FAILED_TO_PARSE_ARRAY, context.tokenStream.getPosition())
    }

    private fun createList(context: ParsingContext, elements: MutableList<Any>): Any? {
        return if (context.token == ListEndToken.TOKEN)
            ImmutableList(elements)
        else
            onError(context, ParserErrorCode.FAILED_TO_PARSE_LIST, context.tokenStream.getPosition())
    }

    private fun createMapOrSet(context: ParsingContext, beginToken: Token, elements: MutableList<Any>): Any? =
        when(beginToken) {
            is MapBeginToken -> createMap(context, elements)
            is SetBeginToken -> createSet(context, elements)
            else -> throw IllegalStateException()
        }

    private fun createMap(context: ParsingContext, elements: MutableList<Any>): Any? {
        return if (context.token == MapOrSetEndToken.TOKEN) {
            val size = elements.size

            if (size % 2 == 0) {
                val map = mutableMapOf<Any, Any>()
                for (i in 0 until size step 2) map[elements[i]] = elements[i + 1]
                ImmutableMap(map)
            } else
                onError(context, ParserErrorCode.FAILED_TO_PARSE_MAP, context.tokenStream.getPosition())
        } else
            onError(context, ParserErrorCode.FAILED_TO_PARSE_MAP, context.tokenStream.getPosition())
    }

    private fun createSet(context: ParsingContext, elements: MutableList<Any>): Any? {
        return if (context.token == MapOrSetEndToken.TOKEN)
            ImmutableSet(elements.toSet())
        else
            onError(context, ParserErrorCode.FAILED_TO_PARSE_SET, context.tokenStream.getPosition())
    }

//    private fun createQuote(element: Any): org.fuusio.flx.core.type.List {
//        val list = mutableListOf<Any>()
//        list.add(Flx.getSymbol(MacroSpec.QUOTE))
//        list.add(element)
//        return ImmutableList(list)
//    }

    private fun onError(
        context: ParsingContext,
        errorCode: ParserErrorCode,
        vararg args: Any
    ): Any? {
        val error = ParserError.create(errorCode, *args)
        context.errors.add(error)
        observer.onError(error)
        return null
    }
}

 

