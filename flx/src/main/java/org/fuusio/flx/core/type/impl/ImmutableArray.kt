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

import com.google.gson.JsonArray
import com.google.gson.JsonPrimitive
import org.fuusio.flx.core.*
import org.fuusio.flx.core.error.InvalidJsonArrayElementException
import org.fuusio.flx.core.toLiteral
import org.fuusio.flx.core.type.*
import org.fuusio.flx.core.type.Array
import org.fuusio.flx.core.type.List
import org.fuusio.flx.core.type.Map
import org.fuusio.flx.core.type.Set
import org.fuusio.flx.core.vm.Ctx
import java.lang.StringBuilder
import java.util.*
import kotlin.collections.ArrayList
import kotlin.reflect.KClass

class ImmutableArray(private val elements: kotlin.collections.List<Any> = emptyList()) : EvaluableObject(), Array {

    override fun conj(tail: Any): Array {
        val list = mutableListOf<Any>()
        list.addAll(elements)
        list.add(tail)
        return create(list)
    }

    override fun cons(head: Any): List {
        val list = mutableListOf<Any>()
        list.add(head)
        list.addAll(elements)
        return ImmutableList.create(list)
    }

    override fun subarray(start: Int, end: Int): Array {
        val slice = mutableListOf<Any>()
        for (i in start until end) slice.add(elements[i])
        return create(slice)
    }

    override fun first() = if (elements.isNotEmpty()) elements.first() else Null

    override fun last() = if (elements.isNotEmpty()) elements.last() else Null

    override fun rest(): Array = if (elements.size < 2) emptyArray else ImmutableArray(elements.subList(1, size()))

    override fun get(index: Int) = if (elements.isNotEmpty()) elements[index] else Null

    override fun isEmpty() = elements.isEmpty()

    override fun isNotEmpty() = elements.isNotEmpty()

    override fun size() = elements.size

    override fun isIndexable() = true

    override fun isArray() = true

    override fun isSequence() = true

    override fun isSizable() = true

    override fun eval(ctx: Ctx): Array {
        val values = mutableListOf<Any>()
        elements.forEach { value -> values.add(value.eval(ctx)) }
        return ImmutableArray(values)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) {
            return true
        }
        return when (other) {
            is ImmutableArray -> {
                this.elements == other.elements
            }
            else -> false
        }
    }

    override fun hashCode() = elements.hashCode()

    override fun toString() = toLiteral()

    fun toKotlinArray(): kotlin.Array<Any> = Array(elements.size) { index -> elements[index] }

    override fun toLiteral(): String {
        val string = StringBuilder()
        elements.forEach { element ->
            if (string.isNotEmpty()) string.append(' ')
            string.append(element.toLiteral())
        }
        return "[$string]"
    }

    override fun toJson(ctx: Ctx): JsonArray {
        val jsonArray = JsonArray()
        elements.forEach { element ->
            val jsonElement = when(element) {
                is String -> JsonPrimitive(element)
                is Boolean -> JsonPrimitive(element)
                is Number -> JsonPrimitive(element)
                is Keyword -> JsonPrimitive(element.toLiteral())
                is Char -> JsonPrimitive(element)
                is Date -> JsonPrimitive(element.toLiteral())
                is Color -> JsonPrimitive(element.toLiteral())
                is List -> JsonPrimitive(element.toLiteral())
                is Array -> element.toJson(ctx)
                is Map -> element.toJson(ctx)
                is Set -> element.toJson(ctx)
                else -> throw InvalidJsonArrayElementException(ctx, element)
            }
            jsonArray.add(jsonElement)
        }
        return jsonArray
    }

    override fun toList() = ImmutableList(elements)

    override fun toSet(): Set {
        val set = mutableSetOf<Any>()
        elements.forEach { element -> set.add(element) }
        return ImmutableSet.create(set)
    }

    override fun isArrayOf(type: FlxType): Boolean =
        isArrayOf(type.getTypeClass())

    private fun isArrayOf(elementType: KClass<*>): Boolean {
        if (elements.isEmpty()) return false
        val javaClass = elementType.java
        elements.forEach { element -> if (!javaClass.isAssignableFrom(element::class.java)) return false }
        return true
    }

    override fun isBooleanArray(): Boolean = isArrayOf(Boolean::class)

    override fun isByteArray(): Boolean = isArrayOf(Byte::class)

    override fun isCharArray(): Boolean = isArrayOf(Char::class)

    override fun isDoubleArray(): Boolean = isArrayOf(Double::class)

    override fun isFloatArray(): Boolean = isArrayOf(Float::class)

    override fun isIntArray(): Boolean = isArrayOf(Int::class)

    override fun isLongArray(): Boolean = isArrayOf(Long::class)

    override fun isStringArray(): Boolean = isArrayOf(String::class)

    override fun toBooleanArray() = BooleanArray(elements.size) { index -> elements[index] as Boolean}

    override fun toByteArray() = ByteArray(elements.size) { index -> elements[index] as Byte}

    override fun toCharArray() = CharArray(elements.size) { index -> elements[index] as Char}

    override fun toDoubleArray() = DoubleArray(elements.size) { index -> elements[index] as Double}

    override fun toFloatArray() = FloatArray(elements.size) { index -> elements[index] as Float}

    override fun toIntArray() = IntArray(elements.size) { index -> elements[index] as Int}

    override fun toLongArray() = LongArray(elements.size) { index -> elements[index] as Long}

    override fun toStringArray() = Array(elements.size) { index -> elements[index].toString()}

    companion object {
        val emptyArray = ImmutableArray()

        fun create(list: ArrayList<Any>) = ImmutableArray(list)

        fun create(list: MutableList<Any>) = ImmutableArray(list)

        fun create(string: String) = ImmutableArray(string.toMutableList())

        fun create(charArray: CharArray) = ImmutableArray(charArray.toList())

        fun create(vararg elements: Any) = ImmutableArray(mutableListOf(*elements))
    }
}