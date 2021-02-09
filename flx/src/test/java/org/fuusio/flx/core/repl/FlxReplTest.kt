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
package org.fuusio.flx.core.repl

import org.fuusio.flx.Flx
import org.fuusio.flx.FlxUnitTest
import org.fuusio.flx.core.Null
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Nested

class FlxReplTest : FlxUnitTest() {

    @Nested
    inner class Test {

        @org.junit.jupiter.api.Test
        fun `(+ 1 2)`() {
            assertEquals(3L, eval("(+ 1 2)"))
        }

        @org.junit.jupiter.api.Test
        fun `(+ 1 (* 2 2) (- 4 2))`() {
            assertNotEquals(3L, eval("(+ 1 (* 2 2) (- 4 2))"))
            assertEquals(7L, eval("(+ 1 (* 2 2) (- 4 2))"))
        }

        @org.junit.jupiter.api.Test
        fun `(and true 1 2)`() {
            assertEquals(true, eval("(and true 1 2)"))
        }

        @org.junit.jupiter.api.Test
        fun `(and true nil 2)`() {
            assertEquals(false, eval("(and true nil 2)"))
        }

        @org.junit.jupiter.api.Test
        fun `(size |1 2 3|)`() {
            assertEquals(3L, eval("(size [1 2 3])"))
        }

        @org.junit.jupiter.api.Test
        fun `'(1 2 3)`() {
            assertEquals(list(1, 2, 3), eval("(quote (1 2 3))"))
        }

        @org.junit.jupiter.api.Test
        fun `(size '(1 2 3)`() {
            assertEquals(3L, eval("(size (quote (1 2 3)))"))
        }

        @org.junit.jupiter.api.Test
        fun `nil`() {
            assertEquals(Null, eval("nil"))
        }

        @org.junit.jupiter.api.Test
        fun `42`() {
            assertEquals(42, eval("42"))
        }

        @org.junit.jupiter.api.Test
        fun `'A'`() {
            assertEquals('A', eval("'A'"))
        }

        @org.junit.jupiter.api.Test
        fun `3dot14`() {
            assertEquals(3.14, eval("3.14"))
        }

        @org.junit.jupiter.api.Test
        fun `"foo"`() {
            assertEquals("foo", eval("\"foo\""))
        }

        @org.junit.jupiter.api.Test
        fun `keyword foo`() {
            val foo = Flx.getKeyword("foo")
            assertEquals(foo, eval(":foo"))
        }

        @org.junit.jupiter.api.Test
        fun `(var foo |A`() {
            assertEquals('A', eval("(var foo 'A') foo"))
        }

        @org.junit.jupiter.api.Test
        fun `'x`() {
            val x = Flx.getSymbol("x")
            assertEquals(x, eval("(quote x)"))
        }

        @org.junit.jupiter.api.Test
        fun `(defn foo |x y| (+ x y)) = foo`() {
            assertEquals(5L, eval("(fun foo [x y] (+ x y)) (foo 2 3)"))
        }
    }
}