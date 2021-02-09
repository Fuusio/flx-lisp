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

import android.os.Bundle
import com.google.gson.JsonObject
import com.google.gson.JsonPrimitive
import org.fuusio.flx.core.*
import org.fuusio.flx.core.error.InvalidBundleValueException
import org.fuusio.flx.core.error.InvalidJsonKeyException
import org.fuusio.flx.core.error.InvalidJsonValueException
import org.fuusio.flx.core.type.*
import org.fuusio.flx.core.type.Array
import org.fuusio.flx.core.type.List
import org.fuusio.flx.core.type.Map
import org.fuusio.flx.core.type.Set
import org.fuusio.flx.core.vm.Ctx
import java.io.Serializable
import java.lang.StringBuilder
import java.util.*

class ImmutableMap(private val pairs: kotlin.collections.Map<Any, Any> = hashMapOf()) : EvaluableObject(), Map {

    override fun get(key: Any): Any = get(key, Null)

    override fun get(key: Any, fallback: Any): Any =
        if (pairs.isNotEmpty()) if (pairs.containsKey(key)) pairs[key] as Any else fallback
        else fallback

    override fun toList(): List {
        val elements = mutableListOf<Any>()
        pairs.keys.forEach { key ->
            elements.add(key)
            elements.add(pairs[key] ?: Null)
        }
        return ImmutableList.create(elements)
    }

    override fun isEmpty() = pairs.isEmpty()

    override fun isNotEmpty() = pairs.isNotEmpty()

    override fun size() = pairs.size

    override fun isMap() = true

    override fun eval(ctx: Ctx): ImmutableMap =
        if (isEmpty())
            emptyMap
        else {
            val values = hashMapOf<Any, Any>()
            pairs.keys.forEach { key -> values[key.eval(ctx)] = (pairs[key] as Any).eval(ctx) }
            ImmutableMap(values)
        }

    override fun equals(other: Any?): Boolean {
        if (this === other) {
            return true
        }
        return when (other) {
            is ImmutableMap -> {
                this.pairs == other.pairs
            }
            else -> false
        }
    }

    override fun hashCode(): Int {
        return pairs.hashCode()
    }

    override fun toArray(): Array {
        val elements = mutableListOf<Any>()
        pairs.keys.forEach { key ->
            elements.add(key)
            elements.add(pairs.getValue(key))
        }
        return ImmutableArray.create(elements)
    }

    override fun toBundle(ctx: Ctx): Bundle {
        val bundle = Bundle()

        pairs.keys.forEach { key ->
            val bundleKey = key.toString()
            when (val value = pairs.getValue(key)) {
                is Array -> {
                    when {
                        value.isBooleanArray() -> bundle.putBooleanArray(bundleKey, value.toBooleanArray())
                        value.isByteArray() -> bundle.putByteArray(bundleKey, value.toByteArray())
                        value.isCharArray() -> bundle.putCharArray(bundleKey, value.toCharArray())
                        value.isDoubleArray() -> bundle.putDoubleArray(bundleKey, value.toDoubleArray())
                        value.isFloatArray() -> bundle.putFloatArray(bundleKey, value.toFloatArray())
                        value.isIntArray() -> bundle.putIntArray(bundleKey, value.toIntArray())
                        value.isLongArray() -> bundle.putLongArray(bundleKey, value.toLongArray())
                        else -> bundle.putStringArray(bundleKey, value.toStringArray())
                    }
                }
                is Boolean -> bundle.putBoolean(bundleKey, value)
                is Byte -> bundle.putByte(bundleKey, value)
                is Char -> bundle.putChar(bundleKey, value)
                is Double -> bundle.putDouble(bundleKey, value)
                is Float -> bundle.putFloat(bundleKey, value)
                is Int -> bundle.putInt(bundleKey, value)
                is Long -> bundle.putLong(bundleKey, value)
                is String -> bundle.putString(bundleKey, value)
                is Serializable -> bundle.putSerializable(bundleKey, value)

                else -> throw InvalidBundleValueException(ctx, value)
            }
        }
        return bundle
    }

    override fun keys(): kotlin.collections.Set<Any> = pairs.keys

    override fun toJson(ctx: Ctx): JsonObject {
        val jsonObject = JsonObject()
        pairs.keys.forEach { key ->
            val propertyKey = when (key) {
                is String -> key
                is Keyword -> key.toLiteral()
                is Char -> key.toLiteral()
                else -> throw InvalidJsonKeyException(ctx, key)
            }

            val propertyValue = when (val value = pairs[key]) {
                is String -> JsonPrimitive(value)
                is Boolean -> JsonPrimitive(value)
                is Number -> JsonPrimitive(value)
                is Keyword -> JsonPrimitive(value.toLiteral())
                is Char -> JsonPrimitive(value.toLiteral())
                is Date -> JsonPrimitive(value.toLiteral())
                is Color -> JsonPrimitive(value.toLiteral())
                is List -> JsonPrimitive(value.toLiteral())
                is Array -> value.toJson(ctx)
                is Map -> value.toJson(ctx)
                is Set -> value.toJson(ctx)
                else -> throw InvalidJsonValueException(ctx, key)
            }
            jsonObject.add(propertyKey, propertyValue)
        }
        return jsonObject
    }

    override fun toSet(): Set {
        val set = mutableSetOf<Any>()
        pairs.keys.forEach { key ->
            set.add(key)
            set.add(pairs.getValue(key))
        }
        return ImmutableSet.create(set)
    }

    override fun toString() = toLiteral()

    override fun toLiteral(): String {
        val string = StringBuilder()
        pairs.forEach { pair ->
            if (string.isNotEmpty()) string.append(' ')
            string.append(pair.key.toLiteral())
            string.append(' ')
            string.append(pair.value.toLiteral())
        }
        return "{$string}"
    }

    fun toKotlinMap(): kotlin.collections.Map<Any, Any> = pairs

    companion object {

        val emptyMap = ImmutableMap()

        fun create(pairs: MutableMap<Any, Any>) = ImmutableMap(pairs)
    }
}