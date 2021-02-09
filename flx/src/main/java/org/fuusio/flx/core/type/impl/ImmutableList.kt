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
package org.fuusio.flx.core.type.impl

import org.fuusio.flx.core.*
import org.fuusio.flx.core.error.NotCallableException
import org.fuusio.flx.core.error.UninitializedSymbolAccessException
import org.fuusio.flx.core.function.FunctionObject
import org.fuusio.flx.core.function.FunctionSymbol
import org.fuusio.flx.core.macro.MacroSpec
import org.fuusio.flx.core.macro.MacroSymbol
import org.fuusio.flx.core.reflection.ReflectionSymbol
import org.fuusio.flx.core.type.List
import org.fuusio.flx.core.toLiteral
import org.fuusio.flx.core.type.Set
import org.fuusio.flx.core.vm.Ctx
import java.lang.StringBuilder

class ImmutableList(private val elements: kotlin.collections.List<Any> = emptyList()) : EvaluableObject(), List {

    override fun conj(tail: Any): List {
        val list = mutableListOf<Any>()
        list.addAll(elements)
        list.add(tail)
        return create(list)
    }

    override fun cons(head: Any): List {
        val list = mutableListOf<Any>()
        list.add(head)
        list.addAll(elements)
        return create(list)
    }

    override fun filter(ctx: Ctx, predicate: FunctionObject): List {
        val filteredElements = mutableListOf<Any>()
        elements.forEach { element-> if (predicate.call(ctx, arrayOf(element)).toBoolean()) filteredElements.add(element) }
        return create(filteredElements)
    }

    override fun first() = if (elements.isNotEmpty()) elements.first() else Null

    override fun last() = if (elements.isNotEmpty()) elements.last() else Null

    override fun rest(): List = if (elements.size < 2) emptyList else ImmutableList(elements.subList(1, size()))

    override fun get(index: Int) = if (elements.isNotEmpty()) elements[index] else Null

    override fun toList(): List = this

    override fun isEmpty() = elements.isEmpty()

    override fun isNotEmpty() = elements.isNotEmpty()

    override fun size() = elements.size

    override fun isIndexable() = true

    override fun isList() = true

    override fun isSequence() = true

    override fun isSizable() = true

    override fun eval(ctx: Ctx): Any = when {
        isNotEmpty() -> {
            val argsCount = elements.size - 1

            when (val head = elements.first()) {
                is FunctionSymbol -> Evaluator.evalFunction(ctx, head.functionSpec, Array(argsCount) { index -> elements[index + 1] })
                is MacroSymbol -> Evaluator.evalMacro(ctx, head.macroSpec, Array(argsCount) { index -> elements[index + 1] })
                is Callable -> head.call(ctx, Array(argsCount) { index -> elements[index + 1].eval(ctx) })
                is ReflectionSymbol -> Evaluator.invoke(ctx, head, Array(argsCount) { index -> elements[index + 1] })
                is Symbol -> {
                    when (val value = head.get(ctx)) {
                        is Callable -> value.call(ctx, Array(argsCount) { index -> elements[index + 1].eval(ctx) })
                        is Null -> throw UninitializedSymbolAccessException(ctx, head)
                        else -> throw NotCallableException(ctx, value)
                    }
                }
                is List -> Evaluator.eval(ctx, head.eval(ctx), Array(argsCount) { index -> elements[index + 1] })
                else -> this
            }
        }
        isEmpty() -> this
        else -> throw IllegalStateException()
    }

    override fun toString() = toLiteral()

    override fun toLiteral(): String {
        if (size() > 0) {
            val head = elements[0]
            if (head is MacroSymbol && head.macroSpec == MacroSpec.QUOTE) return "'${rest()}"
        }
        val string = StringBuilder()
        elements.forEach { element ->
            if (string.isNotEmpty()) string.append(' ')
            string.append(element.toLiteral())
        }
        return "($string)"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) {
            return true
        }
        return when (other) {
            is ImmutableList -> {
               this.elements == other.elements
            }
            else -> false
        }
    }

    override fun hashCode() = elements.hashCode()

    override fun toArray(): org.fuusio.flx.core.type.Array {
        return ImmutableArray.create(elements)
    }

    override fun toSet(): Set {
        val set = mutableSetOf<Any>()
        elements.forEach { element -> set.add(element) }
        return ImmutableSet.create(set)
    }

    fun toKotlinList(): kotlin.collections.List<Any> = elements

    companion object {

        val emptyList = ImmutableList()

        fun create(list: ArrayList<Any>) = ImmutableList(list)

        fun create(list: MutableList<Any>) = ImmutableList(list)

        fun create(charArray: CharArray) = ImmutableList(charArray.toList())

        fun create(vararg elements: Any) = ImmutableList(elements.toList())
    }
}