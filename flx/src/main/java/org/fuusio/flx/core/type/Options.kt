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
package org.fuusio.flx.core.type

import org.fuusio.flx.core.EvaluableObject
import org.fuusio.flx.core.eval
import org.fuusio.flx.core.toBoolean
import org.fuusio.flx.core.vm.Ctx

@Suppress("unused")
open class Options : EvaluableObject() {

    protected val values = mutableMapOf<String, Any>()

    operator fun get(key: String): Any? = values[key]

    operator fun set(key: String, value: Any) {
        values[key] = value
    }

    fun getBoolean(ctx: Ctx, key: String, fallback: Boolean): Boolean =
        (values[key]?.eval(ctx) ?: fallback).toBoolean()

    fun getByte(ctx: Ctx, key: String, fallback: Byte): Byte =
        ((values[key]?.eval(ctx) ?: fallback) as Number).toByte()

    fun getChar(ctx: Ctx, key: String, fallback: Char): Char =
        (values[key]?.eval(ctx) ?: fallback) as Char

    fun getColor(ctx: Ctx, key: String, fallback: Color): Color =
        (values[key]?.eval(ctx) ?: fallback) as Color

    fun getDouble(ctx: Ctx, key: String, fallback: Double): Double =
        ((values[key]?.eval(ctx) ?: fallback) as Number).toDouble()

    fun getFloat(ctx: Ctx, key: String, fallback: Float): Float =
        ((values[key]?.eval(ctx) ?: fallback) as Number).toFloat()

    fun getInt(ctx: Ctx, key: String, fallback: Int): Int =
        ((values[key]?.eval(ctx) ?: fallback) as Number).toInt()

    fun getLong(ctx: Ctx, key: String, fallback: Long): Long =
        ((values[key]?.eval(ctx) ?: fallback) as Number).toLong()

    fun getString(ctx: Ctx, key: String, fallback: String): String =
        (values[key]?.eval(ctx) ?: fallback).toString()

    companion object {
        val empty = Options()
    }
}