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
@file:Suppress("unused")

package org.fuusio.flx.core

import com.google.gson.JsonObject
import com.google.gson.JsonPrimitive
import org.fuusio.flx.core.type.*
import org.fuusio.flx.core.type.Array
import org.fuusio.flx.core.type.List
import org.fuusio.flx.core.type.Map
import org.fuusio.flx.core.type.Set
import org.fuusio.flx.core.type.impl.ImmutableArray
import org.fuusio.flx.core.type.impl.ImmutableList
import org.fuusio.flx.core.type.impl.ImmutableSet
import org.fuusio.flx.core.vm.Ctx
import java.util.*

// ---------------------------------------------------------------------------------------------------------------------
// Any

fun Any.eval(ctx: Ctx): Any =
    when (this) {
        is List -> this.eval(ctx)
        is Symbol -> this.eval(ctx)
        is Code -> eval(ctx)
        is Array -> this.eval(ctx)
        is Set -> this.eval(ctx)
        is Map -> this.eval(ctx)
        is Keyword -> this.eval(ctx)
        is Evaluable -> this.eval(ctx)
        else -> this
    }

fun Any.isArray() = false

fun Any.isBoolean() = false

fun Any.isByte() = false

fun Any.isChar() = false

fun Any.isDouble() = false

fun Any.isFalse() =
    when (this) {
        is Boolean -> this == false
        is Null -> true
        else -> false
    }

fun Any.isFloat() = false

fun Any.isIndexable() = false

fun Any.isInt() = false

fun Any.isList() = false

fun Any.isLong() = true

fun Any.isMap() = false

fun Any.isNil() =
    when (this) {
        is Null -> true
        else -> false
    }

fun Any.isSequence() = false

fun Any.isShort() = false

fun Any.isSet() = false

fun Any.isSizable() = false

fun Any.isString() = false

fun Any.isSymbol() = false

fun Any.isTrue()
    = when (this) {
        is Boolean -> this == true
        is Null -> false
        else -> true
    }

fun Any.toBoolean()
    = when (this) {
        is Boolean -> this
        is Null -> false
        is JsonPrimitive -> asBoolean
        else -> true
}
fun Any.toLiteral(): String
        = when(this) {
    is Char -> "'$this'"
    is Color -> this.toLiteral()
    is String -> "\"$this\""
    is Regex -> "#\"${this.pattern}\""
    is Keyword -> this.toLiteral()
    is JsonObject -> this.toString()
    else -> this.toString()
}

// ---------------------------------------------------------------------------------------------------------------------
// Boolean

fun Boolean.isBoolean() = true

fun Boolean.isFalse() = !this

fun Boolean.isNil() = false

fun Boolean.isTrue() = this

fun Boolean.toBoolean() = this

// ---------------------------------------------------------------------------------------------------------------------
// ArrayList

fun ArrayList<Any>.eval(ctx: Ctx) =
    Evaluator.eval(ctx, ImmutableList.create(this))

// ---------------------------------------------------------------------------------------------------------------------
// Byte

fun Byte.isByte() = true

// ---------------------------------------------------------------------------------------------------------------------
// Char

fun Char.isChar() = true

fun Char.toLiteral() = "'$this'"

// ---------------------------------------------------------------------------------------------------------------------
// Date

fun Date.isDate() = true

fun Date.toLiteral() = this.toString()

// ---------------------------------------------------------------------------------------------------------------------
// Double

fun Double.isDouble() = true

// ---------------------------------------------------------------------------------------------------------------------
// Float

fun Float.isFloat() = true

// ---------------------------------------------------------------------------------------------------------------------
// Int

fun Int.isInt() = true

// ---------------------------------------------------------------------------------------------------------------------
// List

fun kotlin.collections.List<Any>.eval(ctx: Ctx) =
    Evaluator.eval(ctx, ImmutableList.create(this))

// ---------------------------------------------------------------------------------------------------------------------
// Long

fun Long.isLong() = true

// ---------------------------------------------------------------------------------------------------------------------
// Regex

fun Regex.toLiteral() = "#\"$this\""

// ---------------------------------------------------------------------------------------------------------------------
// Short

fun Short.isShort() = true

// ---------------------------------------------------------------------------------------------------------------------
// String

fun String.cons(head: Any): List {
    val list = mutableListOf<Any>()
    list.add(head)
    toCharArray().forEach { char -> list.add(char) }
    return ImmutableList.create(list)
}

fun String.first() = if (isNotEmpty()) this[0] else throw IllegalStateException() // TODO

fun String.isIndexable() = true

fun String.isSequence() = true

fun String.isSizable() = true

fun String.isString() = false

fun String.last() = if (isNotEmpty()) this[length - 1] else throw IllegalStateException() // TODO

fun String.rest() = if (isNotEmpty()) this.subSequence(1, length) else ""

fun String.size() = length

fun String.toArray(): Array = ImmutableArray.create(this.toCharArray())

fun String.toList(): List = ImmutableList.create(this.toCharArray())

fun String.toSet(): Set = ImmutableSet.create(this.toCharArray())


