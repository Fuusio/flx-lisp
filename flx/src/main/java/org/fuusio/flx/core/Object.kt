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

interface Object : Evaluable {

    fun isArray(): Boolean

    fun isBoolean(): Boolean

    fun isByte(): Boolean

    fun isChar(): Boolean

    fun isColor(): Boolean

    fun isDate(): Boolean

    fun isDouble(): Boolean

    fun isFalse(): Boolean

    fun isFloat(): Boolean

    fun isIndexable(): Boolean

    fun isInt(): Boolean

    fun isList(): Boolean

    fun isLong(): Boolean

    fun isMap(): Boolean

    fun isNil(): Boolean

    fun isSequence(): Boolean

    fun isSet(): Boolean

    fun isShort(): Boolean

    fun isSizable(): Boolean

    fun isString(): Boolean

    fun isSymbol(): Boolean

    fun isTrue(): Boolean

    fun toBoolean(): Boolean

    fun toLiteral(): String
}