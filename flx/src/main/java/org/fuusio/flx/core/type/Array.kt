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

import com.google.gson.JsonArray
import org.fuusio.flx.core.vm.Ctx

interface Array : Sequence {

    fun conj(tail: Any): Array

    fun subarray(start: Int, end: Int): Array

    fun isArrayOf(type: FlxType): Boolean

    fun isBooleanArray(): Boolean

    fun isByteArray(): Boolean

    fun isCharArray(): Boolean

    fun isDoubleArray(): Boolean

    fun isFloatArray(): Boolean

    fun isIntArray(): Boolean

    fun isLongArray(): Boolean

    fun isStringArray(): Boolean

    fun toJson(ctx: Ctx): JsonArray

    fun toSet(): Set

    fun toBooleanArray(): BooleanArray

    fun toByteArray(): ByteArray

    fun toCharArray(): CharArray

    fun toDoubleArray(): DoubleArray

    fun toFloatArray(): FloatArray

    fun toIntArray(): IntArray

    fun toLongArray(): LongArray

    fun toStringArray(): kotlin.Array<String>
}