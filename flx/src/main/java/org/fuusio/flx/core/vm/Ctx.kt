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
package org.fuusio.flx.core.vm

import org.fuusio.flx.Program
import org.fuusio.flx.core.*
import org.fuusio.flx.core.error.FlxException
import org.fuusio.flx.core.error.ImmutableSymbolException

open class Ctx(
    open var vm: FlxVM? = null,
    open val parent: Ctx? = null
) {
    private val assignments = hashMapOf<String, Assignment>()

    lateinit var program: Program

    open fun create(): Ctx {
        val childCtx = Ctx(vm = vm, parent = this)
        childCtx.program = program
        return childCtx
    }

    open fun create(vm: FlxVM): Ctx {
        val childCtx = Ctx(vm = vm, parent = this)
        childCtx.program = program
        return childCtx
    }

    fun get(symbol: Symbol): Any =
        assignments[symbol.name]?.get() ?: parent?.get(symbol) ?: Null

    internal fun getAssignment(symbol: Symbol): Assignment? =
        assignments[symbol.name] ?: parent?.getAssignment(symbol)

    fun set(symbol: Symbol, value: Any, isArgValue: Boolean = false) {
        if (isArgValue) {
            addAssignment(symbol, SymbolAssignment(value))
        } else {
            when (val assignment = getAssignment(symbol)) {
                is VarAssignment -> assignment.set(value)
                is SymbolAssignment -> assignment.set(value)
                is ValAssignment -> throw ImmutableSymbolException(this, symbol)
                else -> addAssignment(symbol, SymbolAssignment(value))
            }
        }
    }

    fun hasAssignment(symbol: Symbol): Boolean = assignments.containsKey(symbol.name)

    fun getFlxVm(): FlxVM = vm ?: parent!!.getFlxVm()

    fun canContinue(): Boolean = vm!!.canContinue()

    fun reset() {
        assignments.clear()
        parent?.reset()
    }

    open fun onException(exception: FlxException) {
        parent!!.onException(exception)
    }

    fun addAssignment(symbol: Symbol, assignment: Assignment) {
        assignments[symbol.name] = assignment
    }

    open fun toast(ctx: Ctx, message: String) {
        vm?.out?.println(message)
    }

    companion object {
        val emptyArgs = emptyArray<Any>()
    }
}