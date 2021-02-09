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
package org.fuusio.flx.core.function

import org.fuusio.flx.Flx
import org.fuusio.flx.core.*
import org.fuusio.flx.core.error.InvalidNumberOfArgsException
import org.fuusio.flx.core.vm.Ctx
import java.lang.StringBuilder

class DefinedFunction(override val functionName: Symbol? = null, private val parameters: Array<Symbol>, private val functionBody: Any)
    : AbstractFunctionObject(functionName) {

    init {
        functionName?.set(Flx.ctx, this)
    }

    override fun call(ctx: Ctx, args: Array<Any>): Any {
        val functionCtx = ctx.create()

        if (parameters.size != args.size) {
            throw InvalidNumberOfArgsException(functionCtx, this, args.size, parameters.size)
        }

        for (i in parameters.indices) {
            functionCtx.set(parameters[i], args[i], true)
        }
        return functionBody.eval(functionCtx)
    }

    override fun toString(): String =
        when {
            functionName != null -> "DefinedFunction[ name = \"$functionName\", parameters = [${getParameterList()}], body = ${functionBody.toLiteral()} ]"
            else -> "DefinedFunction[ parameters = [${getParameterList()}], body = ${functionBody.toLiteral()} ]"
        }

    private fun getParameterList(): String {
        val builder = StringBuilder()
        parameters.forEach { symbol ->
            if (builder.isNotEmpty()) builder.append(' ')
            builder.append(symbol.toString())
        }
        return builder.toString()
    }
}