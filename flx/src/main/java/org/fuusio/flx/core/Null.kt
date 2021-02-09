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

import org.fuusio.flx.core.function.FunctionObject
import org.fuusio.flx.core.type.List
import org.fuusio.flx.core.type.impl.ImmutableArray
import org.fuusio.flx.core.type.impl.ImmutableList
import org.fuusio.flx.core.type.impl.ImmutableSet
import org.fuusio.flx.core.vm.Ctx

object Null : EvaluableObject(), List {

    override fun conj(tail: Any): List = ImmutableList.create(mutableListOf(tail))

    override fun filter(ctx: Ctx, predicate: FunctionObject): List = Null

    override fun cons(head: Any) = ImmutableList.create(mutableListOf(head))

    override fun first() = Null

    override fun last() = Null

    override fun rest(): List = Null

    override fun get(index: Int): Any = Null

    override fun toList(): List = Null

    override fun isEmpty() = true

    override fun isFalse() = true

    override fun isIndexable() = false

    override fun isNotEmpty() = false

    override fun isSequence() = true

    override fun isNil() = true

    override fun isList() = true

    override fun isSet() = false

    override fun isTrue() = false

    override fun size() = 0

    override fun toArray() = ImmutableArray.emptyArray

    override fun toBoolean() = false

    override fun toSet() = ImmutableSet.emptySet

    override fun toString() = NAME_NULL
}