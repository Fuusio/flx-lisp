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
import org.fuusio.flx.core.function.DefinedFunction
import org.fuusio.flx.core.function.FunctionSpec
import org.fuusio.flx.core.function.FunctionSymbol
import org.fuusio.flx.core.type.impl.ImmutableList
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class DefinedFunctionTest : FlxUnitTest() {

    @Nested
    inner class Test {

        @org.junit.jupiter.api.Test
        fun `Define function (foo x y)`() {
            val ctx = ctx()
            val foo = Symbol("foo")
            val x = Symbol("x")
            val y = Symbol("y")
            val parameters = arrayOf(x, y)
            val body = ImmutableList.create(
                FunctionSymbol(
                    FunctionSpec.ADDITION
                ), x, y)
            val definedFunction = DefinedFunction(
                foo,
                parameters,
                body
            )
            val list = ImmutableList.create(foo, 1, 2)
            val result = list.eval(ctx)

            Assertions.assertEquals(foo, definedFunction.functionName)
            Assertions.assertTrue(result is Long)
            Assertions.assertEquals(3L, result)
        }
    }
}