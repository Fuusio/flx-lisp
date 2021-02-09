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
package org.fuusio.flx

import org.fuusio.flx.core.error.FlxException
import org.fuusio.flx.core.function.FunctionSpec
import org.fuusio.flx.core.function.FunctionSymbol
import org.fuusio.flx.core.macro.MacroSpec
import org.fuusio.flx.core.macro.MacroSymbol
import org.fuusio.flx.core.repl.FlxRepl
import org.fuusio.flx.core.type.Array
import org.fuusio.flx.core.type.List
import org.fuusio.flx.core.type.Map
import org.fuusio.flx.core.type.Set
import org.fuusio.flx.core.type.impl.ImmutableArray
import org.fuusio.flx.core.type.impl.ImmutableList
import org.fuusio.flx.core.type.impl.ImmutableMap
import org.fuusio.flx.core.type.impl.ImmutableSet
import org.fuusio.flx.core.vm.Ctx
import org.fuusio.flx.core.vm.FlxVM
import java.lang.Exception

abstract class FlxUnitTest {

    fun array(vararg elements: Any): Array {
        val list = mutableListOf<Any>()
        elements.forEach { element ->
            when (element) {
                is FunctionSpec -> list.add(
                    FunctionSymbol(
                        element
                    )
                )
                is MacroSpec -> list.add(
                    MacroSymbol(
                        element
                    )
                )
                else -> list.add(element)
            }
        }
        return ImmutableArray.create(list)
    }

    fun quotedList(vararg elements: Any) = list(MacroSpec.QUOTE, list(*elements))

    fun list(vararg elements: Any): List {
        val list = mutableListOf<Any>()
        elements.forEach { element ->
            when (element) {
                is FunctionSpec -> list.add(FunctionSymbol(element))
                is MacroSpec -> list.add(MacroSymbol(element))
                else -> list.add(element)
            }
        }
        return ImmutableList.create(list)
    }

    fun map(vararg elements: Any): Map {
        val size = elements.size

        if (size % 2 == 0) {
            val map = mutableMapOf<Any, Any>()
            for (i in 0 until size step 2) map[elements[i]] = elements[i + 1]
            return ImmutableMap(map)
        } else
            throw IllegalStateException()
    }

    fun set(vararg elements: Any): Set {
        val set = mutableSetOf<Any>()
        elements.forEach { element ->
            when (element) {
                is FunctionSpec -> set.add(
                    FunctionSymbol(
                        element
                    )
                )
                else -> set.add(element)
            }
        }
        return ImmutableSet.create(set)
    }

    fun ctx(): Ctx {
        initVM()
        return Flx.ctx.create()
    }

    fun eval(vararg elements: Any): Any {
        initVM()
        return eval(Flx.ctx, *elements)
    }

    fun eval(ctx: Ctx, vararg elements: Any): Any {
        initVM()
        return list(*elements).eval(ctx)
    }

    fun eval(string: String) : Any {
        val ctx = initVM()
        return FlxRepl(ctx).eval(string)
    }

    fun evalWithCtx(string: String, programCtx: Ctx) : Any {
        return FlxRepl(programCtx).eval(string)
    }

    internal fun initVM(): Ctx {
        Flx.finalize()
        val rootCtx = RootCtx()
        rootCtx.program = Program()
        Flx.ctx = rootCtx
        val vm = FlxVM(rootCtx)
        val programCtx = Ctx(vm, rootCtx)
        programCtx.program = Program()
        vm.start()
        return programCtx
    }

    class RootCtx : Ctx() {

        var exception: Exception? = null

        override fun onException(exception: FlxException) {
            this.exception = exception
        }
    }
}