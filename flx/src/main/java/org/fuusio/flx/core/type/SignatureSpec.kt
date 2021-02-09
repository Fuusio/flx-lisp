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

import androidx.annotation.StringRes
import org.fuusio.flx.core.FunctionStyle
import org.fuusio.flx.core.error.ArgError
import kotlin.Array

class SignatureSpec(
    @StringRes val description: Int,
    val argSpecs: Array<ArgSpec>,
    var index: Int = 0,
    val style: FunctionStyle = FunctionStyle.LISP_FUNCTION,
)  {

    val firstRepeatableArgIndex: Int
    val repeatableArgsCount: Int

    init {
        var counter = 0
        var firstIndex = -1
        repeat(argSpecs.size) { index ->
            if (argSpecs[index].isRepeatable()) {
                if (firstIndex < 0) firstIndex = index
                counter++
            }
        }
        firstRepeatableArgIndex = firstIndex
        repeatableArgsCount = counter
    }

    fun getArgName(index: Int): String {
        val argSpec = if (index < argSpecs.size) argSpecs[index] else argSpecs.last()
        return argSpec.getArgName()
    }

    fun getArgSpec(index: Int): ArgSpec {
        val argSpecsCount = argSpecs.size
        return if (hasRepeatableArgs()) {
            if (index < firstRepeatableArgIndex) {
                argSpecs[index]
            } else {
                val offset = (index - firstRepeatableArgIndex) % repeatableArgsCount
                argSpecs[firstRepeatableArgIndex + offset]
            }
        } else {
            if (index < argSpecsCount) {
                argSpecs[index]
            } else {
                argSpecs.last()
            }
        }
    }

    fun getArgsCount(): Int  = argSpecs.size

    fun getArgType(index: Int): FlxType = if (index < argSpecs.size) argSpecs[index].type else argSpecs.last().type

    fun checkArgs(args: Array<Any>): Boolean {
        if (checkArgsCount(args)) {
            for (i in args.indices) {
                val type = getArgSpec(i).type
                if (!type.isInstance(args[i])) {
                    return false
                }
            }
            return true
        } else {
            return false
        }
    }

    fun checkArgsCount(argValues: Array<Any>) =
            if (hasRepeatableArgs()) {
                argValues.size >= argSpecs.size &&
                        (argValues.size - firstRepeatableArgIndex) % repeatableArgsCount == 0
            } else {
                if (hasOptionalOrVararg()) {
                    argValues.size >= argSpecs.size - 1
                } else {
                    argValues.size == argSpecs.size
                }
            }

    fun getArgError(args: Array<Any>): ArgError? {
        for (i in args.indices) {
            val argsCount = argSpecs.size
            val type = if (hasVararg() && i >= argsCount) argSpecs.last().type else argSpecs[i].type
            if (!type.isInstance(args[i]))
                return ArgError(i, argSpecs[i].getArgName(), type, args[i]::class)
        }
        return null
    }

    fun hasVararg() = argSpecs.isNotEmpty() && argSpecs.last().isVarArg()

    fun hasOptionalArg() = argSpecs.isNotEmpty() && argSpecs.last().isOptional()

    private fun hasOptionalOrVararg() =
        argSpecs.isNotEmpty() && (argSpecs.last().isOptional() || argSpecs.last().isVarArg())

    fun hasRepeatableArgs() = argSpecs.isNotEmpty() && (argSpecs.last().isRepeatable())
}