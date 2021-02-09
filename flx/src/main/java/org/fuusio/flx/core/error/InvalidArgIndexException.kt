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

import org.fuusio.flx.core.NAME_ANONYMOUS
import org.fuusio.flx.core.vm.Ctx
import org.fuusio.flx.core.function.FunctionObject

class InvalidArgIndexException(ctx: Ctx, private val functionName: String, private val argIndex: Int) : FlxException(ctx) {

    // constructor(ctx: Ctx, functionName: Symbol, argIndex: Int) : this(ctx, functionName.name ?: NAME_ANONYMOUS, argIndex)

    constructor(ctx: Ctx, function: FunctionObject, argIndex: Int) : this(ctx, function.functionName?.name ?: NAME_ANONYMOUS, argIndex)

    override val message: String
        get() = "Invalid arg index of $argIndex used with function '$functionName'."
}