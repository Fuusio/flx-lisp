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
import org.fuusio.flx.core.vm.Ctx
import org.fuusio.flx.core.EvaluableObject
import org.fuusio.flx.core.Keyword
import org.fuusio.flx.core.error.InvalidJsonArrayElementException
import org.fuusio.flx.core.eval
import org.fuusio.flx.core.toLiteral
import org.fuusio.flx.core.type.Array
import org.fuusio.flx.core.type.Color
import org.fuusio.flx.core.type.List
import org.fuusio.flx.core.type.Map
import org.fuusio.flx.core.type.Set
import java.lang.StringBuilder
import java.util.*

class ImmutableSet(private val elements: kotlin.collections.Set<Any> = setOf()) : EvaluableObject(), Set {

    override fun conj(tail: Any): Set {
        val set = mutableSetOf<Any>()
        set.addAll(elements)
        set.add(tail)
        return create(set)
    }

    override fun cons(head: Any): List {
        val list = mutableListOf<Any>()
        list.add(head)
        list.addAll(elements)
        return ImmutableList.create(list)
    }

    override fun toList(): List = ImmutableList.create(elements.toMutableList())

    override fun isEmpty() = elements.isEmpty()

    override fun isNotEmpty() = elements.isNotEmpty()

    override fun size() = elements.size

    override fun isSet() = true

    override fun isSizable() = true

    override fun eval(ctx: Ctx): Set {
        if (elements.isEmpty()) {
            return emptySet
        }
        val values = mutableSetOf<Any>()
        elements.forEach { value -> values.add(value.eval(ctx)) }
        return ImmutableSet(values)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) {
            return true
        }
        return when (other) {
            is ImmutableSet -> {
                this.elements == other.elements
            }
            else -> false
        }
    }

    override fun hashCode(): Int {
        return elements.hashCode()
    }

    override fun toArray(): Array {
        val list = mutableListOf<Any>()
        list.addAll(elements)
        return ImmutableArray.create(list)
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

    override fun toString() = toLiteral()

    override fun toLiteral(): String {
        val string = StringBuilder()
        elements.forEach { element ->
            if (string.isNotEmpty()) string.append(' ')
            string.append(element.toLiteral())
        }
        return "#{$string}"
    }

    fun toKotlinSet(): kotlin.collections.Set<Any> = elements

    companion object {
        val emptySet = ImmutableSet()

        fun create(charArray: CharArray): ImmutableSet {
            val set = mutableSetOf<Any>()
            charArray.forEach { char -> set.add(char) }
            return ImmutableSet(set)
        }

        fun create(elements: kotlin.collections.Set<Any>) = ImmutableSet(elements)
    }

}