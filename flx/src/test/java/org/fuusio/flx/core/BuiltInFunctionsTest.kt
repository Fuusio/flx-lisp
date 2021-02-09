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

import org.fuusio.flx.Flx
import org.fuusio.flx.FlxUnitTest
import org.fuusio.flx.core.error.FlxException
import org.fuusio.flx.core.error.FlxRuntimeException
import org.fuusio.flx.core.error.ValRedefinitionException
import org.fuusio.flx.core.error.VarRedefinitionException
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.fail

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class BuiltInFunctionsTest : FlxUnitTest() {

    @Nested
    inner class RepeatTest {

        @org.junit.jupiter.api.Test
        fun `repeat`() {
            val expression = """
                (var x 10)
                (repeat 10 (-- x))
                x
            """
            eval(expression, 0L)
        }
    }

    @Nested
    inner class AssignTest {

        @org.junit.jupiter.api.Test
        fun `val`() {
            val expression = """
                (val x 42)
                x
            """
            val ctx = eval(expression,42)
            assertTrue(ctx.exception == null)
        }

        @org.junit.jupiter.api.Test
        fun `val re-assign`() {
            val expression = """
                (val x 3)
                (= x 2)
            """
            val ctx = eval(expression, Null)
            assertTrue(ctx.exception is ValRedefinitionException)
        }

        @org.junit.jupiter.api.Test
        fun `val re-assign in function body`() {
            val expression = """
                (val x 42)
                (fun foo [] (val x 0xF00))
                (foo)
                x
            """
            val ctx = eval(expression, 42)
            assertTrue(ctx.exception == null)
        }
    }

    @Nested
    inner class ValTest {

        @org.junit.jupiter.api.Test
        fun `val`() {
            val expression = """
                (val x 2)
                x
            """
            eval(expression,2)
        }

        @org.junit.jupiter.api.Test
        fun `val in block`() {
            val expression = """
                (eval (code (val x 1)))
                x
            """
            eval(expression,1)
        }

        @org.junit.jupiter.api.Test
        fun `val in top and block`() {
            val expression = """
                (val x 3)
                (eval (code (val x 1)))
            """
            val ctx = eval(expression, Null)
            assertTrue(ctx.exception is ValRedefinitionException)
        }
    }

    @Nested
    inner class VarTest {

        @org.junit.jupiter.api.Test
        fun `var`() {
            val expression = """
                (var x 2)
                x
            """
            eval(expression,2)
        }

        @org.junit.jupiter.api.Test
        fun `var in block`() {
            val expression = """
                (eval (code (var x 1)))
                x
            """
            eval(expression,1)
        }

        @org.junit.jupiter.api.Test
        fun `var in top and block`() {
            val expression = """
                (var x 3)
                (eval (code (var x 1)))
            """
            val ctx = eval(expression, Null)
            assertTrue(ctx.exception is VarRedefinitionException)
        }
    }

    @Nested
    inner class DoTest {

        @org.junit.jupiter.api.Test
        fun `do`() {
            val expression = """
                (val x 2)
                (val y 1)
                (do 'a' 'b' (if (> x y) "x is greater" "y is greater"))
            """
            eval(expression,"x is greater")
        }
    }

    @Nested
    inner class IfTest {

        @org.junit.jupiter.api.Test
        fun `if`() {
            val expression = """
                (val x 2)
                (val y 1)
                (if (> x y) "x is greater" "y is greater") 
            """
            eval(expression,"x is greater")
        }
    }

    @Nested
    inner class WhenTest {

        @org.junit.jupiter.api.Test
        fun `when b`() {
            val expression = """
                (val x :b)
                (when
                    (== x :a) 'a'
                    (== x :b) 'b'
                    (== x :c) 'c'
                    true 'x'
                ) 
            """
            eval(expression,'b')
        }

        @org.junit.jupiter.api.Test
        fun `when x`() {
            val expression = """
                (val x :d)
                (when
                    (== x :a) 'a'
                    (== x :b) 'b'
                    (== x :c) 'c'
                    true 'x'
                ) 
            """
            eval(expression,'x')
        }
    }

    @Nested
    inner class CallbackTest {

        @org.junit.jupiter.api.Test
        fun `code`() {
            val expression = """
                (val cb (callback (code (+ x y)) x y)) 
                (eval (cb 1 2))
            """
            eval(expression,3L)
        }
    }

    @Nested
    inner class FunTest {

        @org.junit.jupiter.api.Test
        fun `fun`() {
            val expression = """
                (fun foo [x y] (+ x y))
                (foo 1 2)
            """
            eval(expression,3L)
        }
    }

    @Nested
    inner class FnTest {

        @org.junit.jupiter.api.Test
        fun `fn`() {
            val expression = """
                ((fn [x y] (+ x y)) 1 2)
            """
            eval(expression,3L)
        }
    }

    @Nested
    inner class CodeTest {

        @org.junit.jupiter.api.Test
        fun `code`() {
            val expression = """
                (eval (code (+ 1 2)))
            """
            eval(expression,3L)
        }
    }

    @Nested
    inner class SwitchTest {

        @org.junit.jupiter.api.Test
        fun `switch in fibonacci function`() {
            val expression = """
                (fun fib [n]
                    (switch n 0 1 1 1 _*_else (+ (fib (- n 1)) (fib (- n 2))))  
                )
                (fib 6)
            """
            eval(expression,13L)
        }
    }

    @Nested
    inner class WhileTest {

        @org.junit.jupiter.api.Test
        fun `while loop with decrease`() {
            val expression = """
                (var n 10)
                (while (> n 0) (-- n))
                n
            """
            eval(expression,0L)
        }

        @org.junit.jupiter.api.Test
        fun `while loop with increase`() {
            val expression = """
                (var n 0)
                (while (< n 9) (++ n))
                n
            """
            eval(expression, 9L)
        }
    }

    fun eval(expression: String, expected: Any): RootCtx {
        val programCtx = initVM()
        val rootCtx = Flx.ctx as RootCtx
        try {
            val result =  evalWithCtx(expression, programCtx)
            when  (expected) {
                Null -> {}
                else -> {
                    Assertions.assertEquals(expected, result)
                    assertTrue(rootCtx.exception == null)
                }
            }
        } catch (exception: FlxException) {
            programCtx.onException(exception)
        } catch (exception: Exception) {
            programCtx.onException(FlxRuntimeException(programCtx, exception))
        }
        return rootCtx
    }
}