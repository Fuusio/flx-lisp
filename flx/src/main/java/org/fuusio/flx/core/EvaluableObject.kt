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

import org.fuusio.flx.core.vm.Ctx

abstract class EvaluableObject : Object {

    override fun eval(ctx: Ctx): Any = this

    override fun isArray() = false

    override fun isBoolean() = false

    override fun isByte() = false

    override fun isChar() = false

    override fun isColor() = false

    override fun isDate() = false

    override fun isDouble() = false

    override fun isFalse() = false

    override fun isFloat() = false

    override fun isIndexable() = false

    override fun isInt() = false

    override fun isList() = false

    override fun isMap() = false

    override fun isSequence() = false

    override fun isSet() = false

    override fun isSizable() = false

    override fun isLong() = false

    override fun isNil() = false

    override fun isShort() = false

    override fun isString() = false

    override fun isSymbol() = false

    override fun isTrue() = true

    override fun toBoolean() = true

    override fun toLiteral() = toString()
}