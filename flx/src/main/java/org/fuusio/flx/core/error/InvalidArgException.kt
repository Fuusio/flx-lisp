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
package org.fuusio.flx.core.error

import org.fuusio.flx.core.vm.Ctx
import org.fuusio.flx.Flx
import org.fuusio.flx.core.Symbol
import org.fuusio.flx.core.function.FunctionSymbol
import org.fuusio.flx.core.macro.MacroSpec
import org.fuusio.flx.core.macro.MacroSymbol

class InvalidArgException(ctx: Ctx, private val functionName: Symbol, private val argIndex: Int, private val argName: String, private val description: String) : FlxException(ctx) {

    constructor(ctx: Ctx, macroSpec: MacroSpec, argError: ArgError, description: String)
            : this(ctx, Flx.getSymbol(macroSpec.symbol), argError.argIndex, argError.argName, description)

    override val message: String
        get() = when (functionName) {
            is FunctionSymbol -> "Function '$functionName' received an invalid arg value for parameter '$argName' at index $argIndex: $description."
            is MacroSymbol -> "Macro '$functionName' received an invalid arg value for parameter '$argName' at index $argIndex: $description."
            else -> throw IllegalStateException()
        }
}