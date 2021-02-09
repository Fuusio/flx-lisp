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
package org.fuusio.flx.core

import org.fuusio.flx.FlxUnitTest
import org.fuusio.flx.core.error.InvalidArgTypeException
import org.fuusio.flx.core.function.FunctionSpec
import org.fuusio.flx.core.macro.MacroSpec
import org.fuusio.flx.core.type.Array
import org.fuusio.flx.core.type.List
import org.fuusio.flx.core.type.Sequence
import org.fuusio.flx.core.type.Set
import org.fuusio.flx.core.type.impl.ImmutableArray
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class EvaluatorTest : FlxUnitTest() {

    @Nested
    inner class TestAddition {

        @org.junit.jupiter.api.Test
        fun `(+ 1 2 3) = 6L`() {
            val result = eval(FunctionSpec.ADDITION, 1, 2, 3)

            assertTrue(result is Long)
            assertEquals(6L, result)
        }

        @org.junit.jupiter.api.Test
        fun `(+ 1 2 3dot2) = 6dot2`() {
            val result = eval(FunctionSpec.ADDITION, 1, 2, 3.2)

            assertTrue(result is Double)
            assertEquals(6.2, result)
        }
    }

    @Nested
    inner class TestAnd {

        @org.junit.jupiter.api.Test
        fun `(and true true true) = true`() {
            val result = eval(FunctionSpec.AND, true, true, true)

            assertTrue(result is Boolean)
            assertEquals(true, result)
        }

        @org.junit.jupiter.api.Test
        fun `(and true false true) = false`() {
            val result = eval(FunctionSpec.AND, true, false, true)

            assertTrue(result is Boolean)
            assertEquals(false, result)
        }

        @org.junit.jupiter.api.Test
        fun `(and true 'Foo') = true`() {
            val result = eval(FunctionSpec.AND, true, "Foo")

            assertTrue(result is Boolean)
            assertEquals(true, result)
        }

        @org.junit.jupiter.api.Test
        fun `(and true) = true`() {
            val result = eval(FunctionSpec.AND, true)

            assertTrue(result is Boolean)
            assertEquals(true, result)
        }

        @org.junit.jupiter.api.Test
        fun `(and true nil) = true`() {
            val result = eval(FunctionSpec.AND, true, Null)

            assertTrue(result is Boolean)
            assertEquals(false, result)
        }
    }

    @Nested
    inner class TestButLast {

        @org.junit.jupiter.api.Test
        fun `(butlast 2 "foo") = "f"`() {
            val result = eval(FunctionSpec.BUT_LAST, 2, "foo")

            assertTrue(result is String)
            assertEquals("f", result)
        }

        @org.junit.jupiter.api.Test
        fun `(butlast 4 "foo") = ""`() {
            val result = eval(FunctionSpec.BUT_LAST, 4, "foo")

            assertTrue(result is String)
            assertEquals("", result)
        }
    }

    @Nested
    inner class TestChop {

        @org.junit.jupiter.api.Test
        fun `(chop "foo") = "fo"`() {
            val result = eval(FunctionSpec.CHOP, "foo")

            assertTrue(result is String)
            assertEquals("fo", result)
        }

        @org.junit.jupiter.api.Test
        fun `(chop "") = ""`() {
            val result = eval(FunctionSpec.CHOP, "")

            assertTrue(result is String)
            assertEquals("", result)
        }
    }

    @Nested
    inner class TestConj {

        @org.junit.jupiter.api.Test
        fun `(conj '(1 2 3) 4) = (1 2 3 4)`() {
            val result = eval(FunctionSpec.CONJ, quotedList(1, 2, 3), 4)

            assertTrue(result is List)
            assertEquals(list(1, 2, 3, 4), result)
        }

        @org.junit.jupiter.api.Test
        fun `(conj |1 2 3| 4) = |1 2 3 4|`() {
            val result = eval(FunctionSpec.CONJ, array(1, 2, 3), 4)

            assertTrue(result is Array)
            assertEquals(array(1, 2, 3, 4), result)
        }

        @org.junit.jupiter.api.Test
        fun `(conj #{1 2 3} 4) = #{1 2 3 4}`() {
            val result = eval(FunctionSpec.CONJ, set(1, 2, 3), 4)

            assertTrue(result is Set)
            assertEquals(set(1, 2, 3, 4), result)
        }

        @org.junit.jupiter.api.Test
        fun `(conj nil 4) = (4)`() {
            val result = eval(FunctionSpec.CONJ, Null, 4)

            assertTrue(result is List)
            assertEquals(list(4), result)
        }
    }

    @Nested
    inner class TestCons {

        @org.junit.jupiter.api.Test
        fun `(cons 4 '(1 2 3)) = (4 1 2 3 )`() {
            val result = eval(FunctionSpec.CONS, 4, quotedList(1, 2, 3))

            assertTrue(result is List)
            assertEquals(list(4, 1, 2, 3), result)
        }

        @org.junit.jupiter.api.Test
        fun `(cons 4 |1 2 3|) = (4 1 2 3)`() {
            val result = eval(FunctionSpec.CONS, 4, array(1, 2, 3))

            assertTrue(result is List)
            assertEquals(list(4, 1, 2, 3), result)
        }

        @org.junit.jupiter.api.Test
        fun `(cons 4 #{1 2 3}) = #{4 1 2 3}`() {
            val result = eval(FunctionSpec.CONS, 4, set(1, 2, 3))

            assertTrue(result is List)
            assertEquals(list(4, 1, 2, 3), result)
        }

        @org.junit.jupiter.api.Test
        fun `(cons 4 nil) = (4)`() {
            val result = eval(FunctionSpec.CONS, 4, Null)

            assertTrue(result is List)
            assertEquals(list(4), result)
        }

        @org.junit.jupiter.api.Test
        fun `(cons 4 "abc") = (4 a b c)`() {
            val result = eval(FunctionSpec.CONS, 4, "abc")

            assertTrue(result is List)
            assertEquals(list(4, 'a', 'b', 'c'), result)
        }
    }

    @Nested
    inner class TestVal {
        @org.junit.jupiter.api.Test
        fun `(val foo 42) = foo`() {
            val ctx = ctx()
            val foo = Symbol("foo")
            val result = eval(ctx, MacroSpec.VAL, foo, 42)

            assertTrue(result is Symbol)
            assertEquals(42, ctx.get(foo))
        }
    }

    @Nested
    inner class TestVar {
        @org.junit.jupiter.api.Test
        fun `(var foo 42) = foo`() {
            val ctx = ctx()
            val foo = Symbol("foo")
            val result = eval(ctx, MacroSpec.VAR, foo, 42)

            assertTrue(result is Symbol)
            assertEquals(42, ctx.get(foo))
        }
    }

    @Nested
    inner class TestFun {

        @org.junit.jupiter.api.Test
        fun `(fun foo |x y| (+ x y)) = foo`() {
            val ctx = ctx()
            val foo = Symbol("foo")
            val x = Symbol("x")
            val y = Symbol("y")
            val result = eval(ctx, MacroSpec.FUN, foo, array(x, y), list(FunctionSpec.ADDITION, x, y))

            assertTrue(result is Symbol)
            assertEquals(5L, eval(ctx, foo, 2, 3))
        }
    }

    @Nested
    inner class TestFn {
        @org.junit.jupiter.api.Test
        fun `((fun |x| (+ 1 x)) 6) = 7`() {
            val x = Symbol("x")
            val result = eval(list(MacroSpec.FN,  array(x), list(FunctionSpec.ADDITION, 1, x)), 6)

            assertTrue(result is Long)
            assertEquals(7L, result)
        }
    }

    @Nested
    inner class TestDo {

        @org.junit.jupiter.api.Test
        fun `(do (+ 1 2) (+ 3 4)) = 7`() {
            val result = eval(MacroSpec.DO, list(FunctionSpec.ADDITION, 1, 2), list(FunctionSpec.ADDITION, 3, 4))

            assertTrue(result is Long)
            assertEquals(7L, result)
        }

        @org.junit.jupiter.api.Test
        fun `(do) = nil`() {
            val result = eval(MacroSpec.DO)

            assertTrue(result is Null)
        }
    }

    @Nested
    inner class TestDrop {

        @org.junit.jupiter.api.Test
        fun `(drop 1 "foo") = "oo"`() {
            val result = eval(FunctionSpec.DROP, 1, "foo")

            assertTrue(result is String)
            assertEquals("oo", result)
        }

        @org.junit.jupiter.api.Test
        fun `(drop 4 "foo") = ""`() {
            val result = eval(FunctionSpec.DROP, 4, "foo")

            assertTrue(result is String)
            assertEquals("", result)
        }
    }

    @Nested
    inner class TestFilter {

        @org.junit.jupiter.api.Test
        fun `(filter (fun |x| (GT 4)) '(1 2 3 4 5 6 7 8 9) = (5 6 7 8 9)`() {
            val x = Symbol("x")
            val function = list(MacroSpec.FN,  array(x), list(FunctionSpec.GREATER_THAN, x, 4))
            val result = eval(FunctionSpec.FILTER, function, quotedList(1, 2, 3, 4, 5, 6, 7, 8, 9))

            assertTrue(result is List)
            assertEquals(list(5, 6, 7, 8, 9), result)
        }

        @org.junit.jupiter.api.Test
        fun `(filter (fun |x| (GT 4)) #{1 2 3 4 5 6 7 8 9} = (5 6 7 8 9)`() {
            val x = Symbol("x")
            val function = list(MacroSpec.FN,  array(x), list(FunctionSpec.GREATER_THAN, x, 4))
            val result = eval(FunctionSpec.FILTER, function, set(1, 2, 3, 4, 5, 6, 7, 8, 9))

            assertTrue(result is List)
            assertEquals(list(5, 6, 7, 8, 9), result)
        }
    }

    @Nested
    inner class TestGreaterThan {
        @org.junit.jupiter.api.Test
        fun `(GT 3 2)`() {
            val result = eval(FunctionSpec.LESSER_THAN, 2, 3)

            assertTrue(result is Boolean)
            assertEquals(true, result)
        }

        @org.junit.jupiter.api.Test
        fun `(GT 4 3 2)`() {
            val result = eval(FunctionSpec.LESSER_THAN, 2, 3, 4)

            assertTrue(result is Boolean)
            assertEquals(true, result)
        }

        @org.junit.jupiter.api.Test
        fun `(GT 3 3 2)`() {
            val result = eval(FunctionSpec.LESSER_THAN, 2, 3, 3)

            assertTrue(result is Boolean)
            assertEquals(false, result)
        }

        @org.junit.jupiter.api.Test
        fun `(GT 2 3 2)`() {
            val result = eval(FunctionSpec.LESSER_THAN, 2, 3, 2)

            assertTrue(result is Boolean)
            assertEquals(false, result)
        }
    }

    @Nested
    inner class TestGreaterThanOrEqual {
        @org.junit.jupiter.api.Test
        fun `(GTOE 3 2)`() {
            val result = eval(FunctionSpec.LESSER_THAN, 2, 3)

            assertTrue(result is Boolean)
            assertEquals(true, result)
        }

        @org.junit.jupiter.api.Test
        fun `(GTOE 4 3 2)`() {
            val result = eval(FunctionSpec.LESSER_THAN_OR_EQUAL, 2, 3, 4)

            assertTrue(result is Boolean)
            assertEquals(true, result)
        }

        @org.junit.jupiter.api.Test
        fun `(GTOE 3 3 2)`() {
            val result = eval(FunctionSpec.LESSER_THAN_OR_EQUAL, 2, 3, 3)

            assertTrue(result is Boolean)
            assertEquals(true, result)
        }

        @org.junit.jupiter.api.Test
        fun `(GTOE 2 3 2)`() {
            val result = eval(FunctionSpec.LESSER_THAN_OR_EQUAL, 2, 3, 2)

            assertTrue(result is Boolean)
            assertEquals(false, result)
        }
    }

    @Nested
    inner class TestEquals {
        @org.junit.jupiter.api.Test
        fun `(= 1) = true`() {
            val result = eval(FunctionSpec.EQUAL, 1L)

            assertTrue(result is Boolean)
            assertEquals(true, result)
        }

        @org.junit.jupiter.api.Test
        fun `(= 1 1) = true`() {
            val result = eval(FunctionSpec.EQUAL, 1L, 1L)

            assertTrue(result is Boolean)
            assertEquals(true, result)
        }

        @org.junit.jupiter.api.Test
        fun `(= 1 1 1) = true`() {
            val result = eval(FunctionSpec.EQUAL, 1L, 1L, 1L)

            assertTrue(result is Boolean)
            assertEquals(true, result)
        }

        @org.junit.jupiter.api.Test
        fun `(= 1 2 1) = false`() {
            val result = eval(FunctionSpec.EQUAL, 1L, 2L, 1L)

            assertTrue(result is Boolean)
            assertEquals(false, result)
        }

        @org.junit.jupiter.api.Test
        fun `(= nil nil) = true`() {
            val result = eval(FunctionSpec.EQUAL, Null, Null)

            assertTrue(result is Boolean)
            assertEquals(true, result)
        }
    }

    @Nested
    inner class TestEval {
        @org.junit.jupiter.api.Test
        fun `(eval '(+ 1 2 3))`() {
            val result = eval(FunctionSpec.EVAL, list(MacroSpec.QUOTE, list(FunctionSpec.ADDITION, 1, 2, 3)))

            assertTrue(result is Long)
            assertEquals(6L, result)
        }
    }

    @Nested
    inner class TestGet {
        @org.junit.jupiter.api.Test
        fun `(get {a 1 b 2 c 3} b) = 2`() {
            val a = Keyword.create(":a")
            val b = Keyword.create(":b")
            val c = Keyword.create(":c")
            val result = eval(FunctionSpec.GET, map(a, 1L, b, 2L, c, 3L), b)

            assertTrue(result is Long)
            assertEquals(2L, result)
        }

        @org.junit.jupiter.api.Test
        fun `(get {a 1 b 2 c 3} d) = nil`() {
            val a = Keyword.create(":a")
            val b = Keyword.create(":b")
            val c = Keyword.create(":c")
            val d = Keyword.create(":d")
            val result = eval(FunctionSpec.GET, map(a, 1L, b, 2L, c, 3L), d)

            assertTrue(result is Null)
        }

        @org.junit.jupiter.api.Test
        fun `(get {a 1 b 2 c 3} d 'error') = nil`() {
            val a = Keyword.create(":a")
            val b = Keyword.create(":b")
            val c = Keyword.create(":c")
            val d = Keyword.create(":d")
            val result = eval(FunctionSpec.GET, map(a, 1L, b, 2L, c, 3L), d, "error")

            assertTrue(result is String)
            assertEquals("error", result)
        }
    }

    @Nested
    inner class TestIf{
        @org.junit.jupiter.api.Test
        fun `(if true (+ 1 2) (+ 3 4)) = 3`() {
            val result = eval(MacroSpec.IF, true, list(FunctionSpec.ADDITION, 1, 2), list(FunctionSpec.ADDITION, 3, 4))

            assertTrue(result is Long)
            assertEquals(3L, result)
        }

        @org.junit.jupiter.api.Test
        fun `(if false (+ 1 2) (+ 3 4)) = 7`() {
            val result = eval(MacroSpec.IF, false, list(FunctionSpec.ADDITION, 1, 2), list(FunctionSpec.ADDITION, 3, 4))

            assertTrue(result is Long)
            assertEquals(7L, result)
        }

        @org.junit.jupiter.api.Test
        fun `(if nil (+ 1 2) (+ 3 4)) = 7`() {
            val result = eval(MacroSpec.IF, false, list(FunctionSpec.ADDITION, 1, 2), list(FunctionSpec.ADDITION, 3, 4))

            assertTrue(result is Long)
            assertEquals(7L, result)
        }

        @org.junit.jupiter.api.Test
        fun `(if (= 2 2) (+ 1 2) (+ 3 4)) = 3`() {
            val result = eval(MacroSpec.IF, list(FunctionSpec.EQUAL, 2, 2), list(FunctionSpec.ADDITION, 1, 2), list(FunctionSpec.ADDITION, 3, 4))

            assertTrue(result is Long)
            assertEquals(3L, result)
        }
    }

    @Nested
    inner class TestLesserThan {
        @org.junit.jupiter.api.Test
        fun `(LT 2 3)`() {
            val result = eval(FunctionSpec.LESSER_THAN, 2, 3)

            assertTrue(result is Boolean)
            assertEquals(true, result)
        }

        @org.junit.jupiter.api.Test
        fun `(LT 2 3 4)`() {
            val result = eval(FunctionSpec.LESSER_THAN, 2, 3, 4)

            assertTrue(result is Boolean)
            assertEquals(true, result)
        }

        @org.junit.jupiter.api.Test
        fun `(LT 2 3 3)`() {
            val result = eval(FunctionSpec.LESSER_THAN, 2, 3, 3)

            assertTrue(result is Boolean)
            assertEquals(false, result)
        }

        @org.junit.jupiter.api.Test
        fun `(LT 2 3 2)`() {
            val result = eval(FunctionSpec.LESSER_THAN, 2, 3, 2)

            assertTrue(result is Boolean)
            assertEquals(false, result)
        }
    }

    @Nested
    inner class TestLesserThanOrEqual {
        @org.junit.jupiter.api.Test
        fun `(LTOE 2 3)`() {
            val result = eval(FunctionSpec.LESSER_THAN, 2, 3)

            assertTrue(result is Boolean)
            assertEquals(true, result)
        }

        @org.junit.jupiter.api.Test
        fun `(LTOE 2 3 4)`() {
            val result = eval(FunctionSpec.LESSER_THAN_OR_EQUAL, 2, 3, 4)

            assertTrue(result is Boolean)
            assertEquals(true, result)
        }

        @org.junit.jupiter.api.Test
        fun `(LTOE 2 3 3)`() {
            val result = eval(FunctionSpec.LESSER_THAN_OR_EQUAL, 2, 3, 3)

            assertTrue(result is Boolean)
            assertEquals(true, result)
        }

        @org.junit.jupiter.api.Test
        fun `(LTOE 2 3 2)`() {
            val result = eval(FunctionSpec.LESSER_THAN_OR_EQUAL, 2, 3, 2)

            assertTrue(result is Boolean)
            assertEquals(false, result)
        }
    }

    @Nested
    inner class TestLet {
        @org.junit.jupiter.api.Test
        fun `(let |x 1 y 2| (+ x y))`() {
            val ctx = ctx()
            val x = Symbol("x")
            val y = Symbol("y")
            val result = eval(ctx, MacroSpec.LET, array(x, 1, y, 2), list(FunctionSpec.ADDITION, x, y))

            assertTrue(result is Long)
            assertEquals(3L, result)
        }
    }

    @Nested
    inner class TestMatches {

        @org.junit.jupiter.api.Test
        fun `(matches "abcd" "a(|bc|+)d?") = true`() {
            val result = eval(FunctionSpec.MATCHES, "abcd", "a([bc]+)d?")

            assertTrue(result is Boolean)
            assertTrue(result as Boolean)
        }

        @org.junit.jupiter.api.Test
        fun `(matches "xbcd" "x(|bc|+)d?") = true`() {
            val result = eval(FunctionSpec.MATCHES, "xbcd", "x([bc]+)d?".toRegex())

            assertTrue(result is Boolean)
            assertTrue(result as Boolean)
        }

        @org.junit.jupiter.api.Test
        fun `(matches "is" 1234) = true`() {
            try {
                eval(FunctionSpec.MATCHES, "is", 1234)
                Assertions.fail<Boolean>("Should have failed")
            } catch(e: InvalidArgTypeException) {
                // Succeeded
            }
        }

        @org.junit.jupiter.api.Test
        fun `(matches "mk" "dot s") = false`() {
            val result = eval(FunctionSpec.MATCHES, "mk", ".s")

            assertTrue(result is Boolean)
            assertFalse(result as Boolean)
        }
    }

    @Nested
    inner class TestMultiplication {

        @org.junit.jupiter.api.Test
        fun `(* 1 2 3) = 6L`() {
            val result = eval(FunctionSpec.MULTIPLICATION, 1, 2, 3)

            assertTrue(result is Long)
            assertEquals(6L, result)
        }

        @org.junit.jupiter.api.Test
        fun `(* 1 2 3dot2) = 6dot4`() {
            val result = eval(FunctionSpec.MULTIPLICATION, 1, 2, 3.2)

            assertTrue(result is Double)
            assertEquals(6.4, result)
        }
    }

    @Nested
    inner class TestDivision {

        @org.junit.jupiter.api.Test
        fun `(divide 6 2) = 3L`() {
            val result = eval(FunctionSpec.DIVISION, 6, 2)

            assertTrue(result is Double)
            assertEquals(3.0, result)
        }

        @org.junit.jupiter.api.Test
        fun `(divide 10 100) = 0dot1`() {
            val result = eval(FunctionSpec.DIVISION, 10, 100)

            assertTrue(result is Double)
            assertEquals(0.1, result)
        }
    }

    @Nested
    inner class TestNot {

        @org.junit.jupiter.api.Test
        fun `(not true) = false`() {
            val result = eval(FunctionSpec.NOT, true)

            assertTrue(result is Boolean)
            assertEquals(false, result)
        }

        @org.junit.jupiter.api.Test
        fun `(not false) = true`() {
            val result = eval(FunctionSpec.NOT, false)

            assertTrue(result is Boolean)
            assertEquals(true, result)
        }

        @org.junit.jupiter.api.Test
        fun `(not nil) = true`() {
            val result = eval(FunctionSpec.NOT, Null)

            assertTrue(result is Boolean)
            assertEquals(true, result)
        }

        @org.junit.jupiter.api.Test
        fun `(not 42) = false`() {
            val result = eval(FunctionSpec.NOT, 42L)

            assertTrue(result is Boolean)
            assertEquals(false, result)
        }
    }

    @Nested
    inner class TestNotEquals {
        @org.junit.jupiter.api.Test
        fun `(not= 1) = false`() {
            val result = eval(FunctionSpec.NOT_EQUAL, 1L)

            assertTrue(result is Boolean)
            assertEquals(false, result)
        }

        @org.junit.jupiter.api.Test
        fun `(not= 1 1) = false`() {
            val result = eval(FunctionSpec.NOT_EQUAL, 1L, 1L)

            assertTrue(result is Boolean)
            assertEquals(false, result)
        }

        @org.junit.jupiter.api.Test
        fun `(not= 1 1 1) = false`() {
            val result = eval(FunctionSpec.NOT_EQUAL, 1L, 1L, 1L)

            assertTrue(result is Boolean)
            assertEquals(false, result)
        }

        @org.junit.jupiter.api.Test
        fun `(not= 1 2 1) = true`() {
            val result = eval(FunctionSpec.NOT_EQUAL, 1L, 2L, 1L)

            assertTrue(result is Boolean)
            assertEquals(true, result)
        }

        @org.junit.jupiter.api.Test
        fun `(not= nil nil) = false`() {
            val result = eval(FunctionSpec.NOT_EQUAL, Null, Null)

            assertTrue(result is Boolean)
            assertEquals(false, result)
        }
    }

    @Nested
    inner class TestNth {

        @org.junit.jupiter.api.Test
        fun `(nth |0 1 2| 1) = 1`() {
            val result = eval(FunctionSpec.NTH, array(0L, 1L, 2L), 1L)

            assertTrue(result is Long)
            assertEquals(1L, result)
        }

        @org.junit.jupiter.api.Test
        fun `(nth (0 1 2 3 4) 2) = 2`() {
            val result = eval(FunctionSpec.NTH, list(0L, 1L, 2L, 3L, 4L), 2L)

            assertTrue(result is Long)
            assertEquals(2L, result)
        }
    }

    @Nested
    inner class TestOr {

        @org.junit.jupiter.api.Test
        fun `(or false false false) = false`() {
            val result = eval(FunctionSpec.OR, false, false, false)

            assertTrue(result is Boolean)
            assertEquals(false, result)
        }

        @org.junit.jupiter.api.Test
        fun `(or false false true) = true`() {
            val result = eval(FunctionSpec.OR, true, false, true)

            assertTrue(result is Boolean)
            assertEquals(true, result)
        }

        @org.junit.jupiter.api.Test
        fun `(or false "Foo") = true`() {
            val result = eval(FunctionSpec.OR, true, "Foo")

            assertTrue(result is Boolean)
            assertEquals(true, result)
        }

        @org.junit.jupiter.api.Test
        fun `(or true) = true`() {
            val result = eval(FunctionSpec.OR, true)

            assertTrue(result is Boolean)
            assertEquals(true, result)
        }

        @org.junit.jupiter.api.Test
        fun `(or true nil) = true`() {
            val result = eval(FunctionSpec.OR, true, Null)

            assertTrue(result is Boolean)
            assertEquals(true, result)
        }
    }

    @Nested
    inner class TestReadString {

        @org.junit.jupiter.api.Test
        fun `(read-string "123")`() {
            val result = eval(FunctionSpec.PARSE, "123")

            assertTrue(result is Int)
            assertEquals(123, result)
        }

        @org.junit.jupiter.api.Test
        fun `(read-string "123B")`() {
            val result = eval(FunctionSpec.PARSE, "123B")

            assertTrue(result is Byte)
            assertEquals(123.toByte(), result)
        }

        @org.junit.jupiter.api.Test
        fun `(read-string "123L")`() {
            val result = eval(FunctionSpec.PARSE, "123L")

            assertTrue(result is Long)
            assertEquals(123L, result)
        }
    }

    @Nested
    inner class TestReplaceRe {

        @org.junit.jupiter.api.Test
        fun `(replace-re #"foo" " " "fooHellofooWorld!foo")`() {
            val result = eval(FunctionSpec.REPLACE_REGEX, "foo".toRegex(), " ", "fooHellofooWorld!foo")

            assertTrue(result is String)
            assertEquals(" Hello World! ", result)
        }
    }

    @Nested
    inner class TestRest {

        @org.junit.jupiter.api.Test
        fun `(rest "Foo") = |''o', 'o'|`() {
            val result = eval(FunctionSpec.REST, "Foo")

            assertTrue(result is ImmutableArray)
            val expected = array('o', 'o')
            val expectedSize = expected.size()
            val sequence = result as Sequence

            assertEquals(expectedSize, sequence.size())

            repeat(expectedSize) {index ->
                assertEquals(expected[index], sequence[index])
            }
        }

        @org.junit.jupiter.api.Test
        fun `(rest |'F', 'o', 'o'|) = |''o', 'o'|`() {
            val result = eval(FunctionSpec.REST, array('F', 'o', 'o'))

            assertTrue(result is ImmutableArray)
            val expected = array('o', 'o')
            val expectedSize = expected.size()
            val sequence = result as Sequence

            assertEquals(expectedSize, sequence.size())

            repeat(expectedSize) {index ->
                assertEquals(expected[index], sequence[index])
            }
        }
    }

    @Nested
    inner class TestSize {

        @org.junit.jupiter.api.Test
        fun `(size "Foo") = 3L`() {
            val result = eval(FunctionSpec.SIZE, "Foo")

            assertTrue(result is Long)
            assertEquals(3L, result)
        }
    }

    @Nested
    inner class TestSplitLines {

        @org.junit.jupiter.api.Test
        fun `(split-lines "foo bar foo") = |"foo" "bar" "foo"|`() {
            val result = eval(FunctionSpec.SPLIT_LINES, "foo\nbar\r\nfoo")

            assertTrue(result is Array)
            assertEquals(array("foo", "bar", "foo"), result)
        }
    }

    @Nested
    inner class TestStr {

        @org.junit.jupiter.api.Test
        fun `(str "foo") = "foo"`() {
            val result = eval(FunctionSpec.CONC, "foo")

            assertTrue(result is String)
            assertEquals("foo", result)
        }

        @org.junit.jupiter.api.Test
        fun `(str "foo" "bar) = "foobar"`() {
            val result = eval(FunctionSpec.CONC, "foo", "bar")

            assertTrue(result is String)
            assertEquals("foobar", result)
        }

        @org.junit.jupiter.api.Test
        fun `(str) = ""`() {
            val result = eval(FunctionSpec.CONC)

            assertTrue(result is String)
            assertEquals("", result)
        }

        @org.junit.jupiter.api.Test
        fun `(str 1 2 3 4 5 6 7 8 9 0) = "1234567890"`() {
            val result = eval(FunctionSpec.CONC, 1, 2, 3, 4, 5, 6, 7, 8, 9, 0)

            assertTrue(result is String)
            assertEquals("1234567890", result)
        }
    }

    @Nested
    inner class TestContains {

        @org.junit.jupiter.api.Test
        fun `(contains "foo" "Hello foobar") = true`() {
            val result = eval(FunctionSpec.CONTAINS, "foo", "Hello foobar")

            assertTrue(result is Boolean)
            assertEquals(true, result)
        }

        @org.junit.jupiter.api.Test
        fun `(contains "faa" "Hello foobar") = false`() {
            val result = eval(FunctionSpec.CONTAINS, "faa", "Hello foobar")

            assertTrue(result is Boolean)
            assertEquals(false, result)
        }
    }

    @Nested
    inner class TestSubtraction {

        @org.junit.jupiter.api.Test
        fun `(- 6 2) = 4L`() {
            val result = eval(FunctionSpec.SUBTRACTION, 6, 2)

            assertTrue(result is Long)
            assertEquals(4L, result)
        }

        @org.junit.jupiter.api.Test
        fun `(- 6 2do5) = 3dot5`() {
            val result = eval(FunctionSpec.SUBTRACTION, 6, 2.5)

            assertTrue(result is Double)
            assertEquals(3.5, result)
        }
    }

    @Nested
    inner class TestTail {

        @org.junit.jupiter.api.Test
        fun `(tail 2 "foo") = "oo"`() {
            val result = eval(FunctionSpec.TAIL, 2, "foo")

            assertTrue(result is String)
            assertEquals("oo", result)
        }

        @org.junit.jupiter.api.Test
        fun `(tail 4 "foo") = ""`() {
            val result = eval(FunctionSpec.TAIL, 4, "foo")

            assertTrue(result is String)
            assertEquals("", result)
        }
    }

    @Nested
    inner class TestTake {

        @org.junit.jupiter.api.Test
        fun `(take 2 "foo") = "fo"`() {
            val result = eval(FunctionSpec.TAKE, 2, "foo")

            assertTrue(result is String)
            assertEquals("fo", result)
        }

        @org.junit.jupiter.api.Test
        fun `(take 4 "foo") = ""`() {
            val result = eval(FunctionSpec.TAKE, 4, "foo")

            assertTrue(result is String)
            assertEquals("foo", result)
        }
    }

    @Nested
    inner class TestToBoolean {

        @org.junit.jupiter.api.Test
        fun `(to-boolean 6) = true`() {
            val result = eval(FunctionSpec.TO_BOOLEAN, 6)

            assertTrue(result is Boolean)
            assertEquals(true, result)
        }

        @org.junit.jupiter.api.Test
        fun `(to-boolean true) = true`() {
            val result = eval(FunctionSpec.TO_BOOLEAN, true)

            assertTrue(result is Boolean)
            assertEquals(true, result)
        }

        @org.junit.jupiter.api.Test
        fun `(to-boolean false) = false`() {
            val result = eval(FunctionSpec.TO_BOOLEAN, false)

            assertTrue(result is Boolean)
            assertEquals(false, result)
        }

        @org.junit.jupiter.api.Test
        fun `(to-boolean nil) = false`() {
            val result = eval(FunctionSpec.TO_BOOLEAN, Null)

            assertTrue(result is Boolean)
            assertEquals(false, result)
        }
    }

    @Nested
    inner class TestXor {

        @org.junit.jupiter.api.Test
        fun `(xor false false false) = false`() {
            val result = eval(FunctionSpec.XOR, false, false, false)

            assertTrue(result is Boolean)
            assertEquals(false, result)
        }

        @org.junit.jupiter.api.Test
        fun `(xor false false true) = true`() {
            val result = eval(FunctionSpec.XOR, false, false, true)

            assertTrue(result is Boolean)
            assertEquals(true, result)
        }

        @org.junit.jupiter.api.Test
        fun `(xor false true true) = false`() {
            val result = eval(FunctionSpec.XOR, true, false, true)

            assertTrue(result is Boolean)
            assertEquals(false, result)
        }

        @org.junit.jupiter.api.Test
        fun `(xor false "Foo") = true`() {
            val result = eval(FunctionSpec.XOR, false, "Foo")

            assertTrue(result is Boolean)
            assertEquals(true, result)
        }

        @org.junit.jupiter.api.Test
        fun `(xor true) = true`() {
            val result = eval(FunctionSpec.XOR, true)

            assertTrue(result is Boolean)
            assertEquals(true, result)
        }

        @org.junit.jupiter.api.Test
        fun `(xor true nil false) = true`() {
            val result = eval(FunctionSpec.XOR, true, Null, false)

            assertTrue(result is Boolean)
            assertEquals(true, result)
        }
    }

    @Nested
    inner class TestIsZero {

        @org.junit.jupiter.api.Test
        fun `(isZero 0) = true`() {
            val result = eval(FunctionSpec.IS_ZERO, 0)

            assertTrue(result is Boolean)
            assertEquals(true, result)
        }

        @org.junit.jupiter.api.Test
        fun `(isZero 0dot0f) = true`() {
            val result = eval(FunctionSpec.IS_ZERO, 0.0f)

            assertTrue(result is Boolean)
            assertEquals(true, result)
        }

        @org.junit.jupiter.api.Test
        fun `(isZero 0dot0) = true`() {
            val result = eval(FunctionSpec.IS_ZERO, 0.0)

            assertTrue(result is Boolean)
            assertEquals(true, result)
        }

        @org.junit.jupiter.api.Test
        fun `(isZero 0dot1) = false`() {
            val result = eval(FunctionSpec.IS_ZERO, 0.1)

            assertTrue(result is Boolean)
            assertEquals(false, result)
        }
    }
}