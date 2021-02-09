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

import android.os.Handler
import android.os.Looper
import org.fuusio.flx.core.error.InvalidNumberOfArgsException
import org.fuusio.flx.core.function.AbstractFunctionObject
import org.fuusio.flx.core.vm.Ctx
import java.lang.StringBuilder

open class Callback(private val parameters: Array<Symbol>, private val code: Code)
    : AbstractFunctionObject(null) {

    override fun call(ctx: Ctx, args: Array<Any>): Any {
        val callbackCtx = ctx.create()

        if (parameters.size != args.size) {
            ctx.onException(InvalidNumberOfArgsException(ctx, this, args.size, parameters.size))
            return Null
        }

        for (i in parameters.indices) {
            callbackCtx.set(parameters[i], args[i], true)
        }
        return code.eval(callbackCtx)
    }

    override fun toString(): String =
        "Procedure[ name = \"$functionName\", parameters = [${getParameterList()}], body = ${code.toLiteral()} ]"

    private fun getParameterList(): String {
        val builder = StringBuilder()
        parameters.forEach { symbol ->
            if (builder.isNotEmpty()) builder.append(' ')
            builder.append(symbol.toString())
        }
        return builder.toString()
    }

    companion object {
        fun invoke(ctx: Ctx, callback: Callback, vararg args: Any) {
            Handler(Looper.getMainLooper()).post {
                callback.call(ctx, arrayOf(*args))
            }
        }
    }
}