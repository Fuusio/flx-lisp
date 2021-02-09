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
import org.fuusio.flx.core.FunctionFormSpec
import org.fuusio.flx.core.Symbol
import org.fuusio.flx.core.function.FunctionSpec
import org.fuusio.flx.core.macro.MacroSpec
import kotlin.reflect.KClass

class InvalidArgTypeException(ctx: Ctx, private val functionName: Symbol, private val argIndex: Int, private val argName: String, private val wrongArgType: KClass<*>) : FlxException(ctx) {

    constructor(ctx: Ctx, functionSpec: FunctionSpec, argError: ArgError, wrongArgType: KClass<*>)
            : this(ctx, Flx.getSymbol(functionSpec.symbol), argError.argIndex, argError.argName, wrongArgType)

    constructor(ctx: Ctx, functionSpec: FunctionFormSpec, argError: ArgError)
            : this(ctx, Flx.getSymbol(functionSpec), argError.argIndex, argError.argName, argError.actualType)

    constructor(ctx: Ctx, macroSpec: MacroSpec, argError: ArgError)
            : this(ctx, Flx.getSymbol(macroSpec), argError.argIndex, argError.argName, argError.actualType)

    override val message: String
        get() = "Function '$functionName' received an invalid value of type '${wrongArgType.simpleName}' for parameter '$argName' at parameter index $argIndex."
}