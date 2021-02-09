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

open class Symbol(val name: String) : EvaluableObject(), Variable {

    override fun get(ctx: Ctx) = ctx.get(this)

    override fun set(ctx: Ctx, value: Any) {
        ctx.set(this, value)
    }

    override fun eval(ctx: Ctx): Any = ctx.get(this)

    override fun isSymbol() = true

    override fun toString() = name

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as Symbol
        return name == other.name
    }

    override fun hashCode(): Int = name.hashCode()
}