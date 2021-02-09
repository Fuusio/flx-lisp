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
import org.fuusio.flx.FlxUnitTest
import org.fuusio.flx.core.Null
import org.fuusio.flx.core.function.FunctionSpec
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class FormParserTest : FlxUnitTest() {

    @Nested
    inner class TestIntegers {

        @org.junit.jupiter.api.Test
        fun `parse 123`() {
            parse("123", 123)
        }

        @org.junit.jupiter.api.Test
        fun `parse 123L`() {
            parse("123L", 123L)
        }

        @org.junit.jupiter.api.Test
        fun `parse 123B`() {
            parse("123B", 123.toByte())
        }

        @org.junit.jupiter.api.Test
        fun `parse -123`() {
            parse("-123", -123)
        }

        @org.junit.jupiter.api.Test
        fun `parse long 0xFF`() {
            parse("0xFF", 255)
        }

        @org.junit.jupiter.api.Test
        fun `parse int 0xFFBL`() {
            parse("0xFFBL", 0xFFBL)
        }

        @org.junit.jupiter.api.Test
        fun `parse long 2r111`() {
            parse("2r111", 0b111)
        }

        @org.junit.jupiter.api.Test
        fun `parse long 16raBc`() {
            parse("16raBc", 0xABC)
        }
    }

    @Nested
    inner class TestDouble {

        @org.junit.jupiter.api.Test
        fun `parse -123 dot 45`() {
            parse("-123.45", -123.45)
        }

        @org.junit.jupiter.api.Test
        fun `parse double 3 dot 14`() {
            parse("3.14", 3.14)
        }
    }

    @Nested
    inner class TestSymbol {

        @org.junit.jupiter.api.Test
        fun `parse -`() {
            if (!Flx.isInitialized()) initVM()
            val minus = Flx.getSymbol(FunctionSpec.SUBTRACTION)
            parse(
                "-", minus
            )
        }

        @org.junit.jupiter.api.Test
        fun `parse pi`() {
            parse("pi", Math.PI)
        }

        @org.junit.jupiter.api.Test
        fun `parse true`() {
            parse("true", true)
        }

        @org.junit.jupiter.api.Test
        fun `parse false`() {
            parse("false", false)
        }

        @org.junit.jupiter.api.Test
        fun `parse symbol foo`() {
            val foo = Flx.getSymbol("foo")
            parse("foo", foo)
        }

        @org.junit.jupiter.api.Test
        fun `parse nil`() {
            parse("nil", Null)
        }
    }

    @Nested
    inner class TestChar {

        @org.junit.jupiter.api.Test
        fun `parse char a`() {
            parse("'a'", 'a')
        }

        @org.junit.jupiter.api.Test
        fun `parse char space`() {
            parse("' '", ' ')
        }

        @org.junit.jupiter.api.Test
        fun `parse char Ω`() {
            parse("'\\u03A9'", '\u03A9')
        }
    }

    @Nested
    inner class TestRegex {

        @org.junit.jupiter.api.Test
        fun `parse regex #"|0-9|+"`() {
            val regex = parse("#\"[0-9]+\"")
            assertEquals("[0-9]+", regex.toString())
        }
    }

    @Nested
    inner class TestString {

        @org.junit.jupiter.api.Test
        fun `parse string "foo"`() {
            parse("\"foo\"", "foo")
        }
    }

    @Nested
    inner class TestKeyword {

        @org.junit.jupiter.api.Test
        fun `parse keyword bar`() {
            val bar = Flx.getKeyword("bar")
            parse(":bar", bar)
        }
    }

    @Nested
    inner class TestArray {

        @org.junit.jupiter.api.Test
        fun `parse array 1 2 3`() {
            parse("[1 2 3]", array(1, 2, 3))
        }
    }

    @Nested
    inner class TestList {

        @org.junit.jupiter.api.Test
        fun `parse list (1L 2L 3L)`() {
            parse("(1L 2L 3L)", list(1L, 2L, 3L))
        }

//        @org.junit.jupiter.api.Test
//        fun `parse list '(1 2 3)`() {
//            val quote = Flx.getSymbol(MacroSpec.QUOTE)
//            parse("'(1, 2, 3)", list(quote, list(1L, 2L, 3L)))
//        }
//
//        @org.junit.jupiter.api.Test
//        fun `parse list '((1) 2 (3 (4)))`() {
//            val quote = Flx.getSymbol(MacroSpec.QUOTE)
//            parse("((1) 2 (3 (4)))", list(list(1L), 2L, list(3L, list(4L))))
//        }
    }

    @Nested
    inner class TestMap {

        @org.junit.jupiter.api.Test
        fun `parse map a 1 b 2 c 3`() {
            val a = Flx.getKeyword("a")
            val b = Flx.getKeyword("b")
            val c = Flx.getKeyword("c")
            parse("{:a 1 :b 2 :c 3}", map(a, 1, b, 2, c, 3))
        }
    }

    @Nested
    inner class TestSet {

        @org.junit.jupiter.api.Test
        fun `parse set 1 2 3`() {
            parse("#{1 2 2 3}", set(1, 2, 3))
        }
    }

    private fun parse(expression: String): Any {
        val context = ParsingContext()
        val observer = createObserver()
        val parser = FormParser(observer)
        val results = parser.parse(context, expression)

        assertEquals(1, results.size)
        return results[0]
    }

    private fun parse(expression: String, expected: Any) {
        val context = ParsingContext()
        val observer = createObserver()
        val parser = FormParser(observer)
        val results = parser.parse(context, expression)

        assertEquals(1, results.size)
        assertEquals(expected, results[0])
    }

    private fun createObserver() = object : ParserObserver {
        override fun onNext(parsingResult: Any) {
            // Ignore
        }

        override fun onError(error: ParserError) {
            fail<Any>(error.description)
        }

    }
}