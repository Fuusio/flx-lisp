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

import android.util.Log
import org.fuusio.flx.core.error.*
import org.fuusio.flx.core.function.FunctionObject
import org.fuusio.flx.core.function.FunctionSpec
import org.fuusio.flx.core.function.FunctionSymbol
import org.fuusio.flx.core.macro.MacroSpec
import org.fuusio.flx.core.macro.MacroSymbol
import org.fuusio.flx.core.reflection.ReflectionSymbol
import org.fuusio.flx.core.type.List
import org.fuusio.flx.core.vm.Ctx
import java.lang.reflect.Method

object Evaluator {

    fun eval(ctx: Ctx, list: List): Any {
        return when {
            list.isNotEmpty() -> {
                val argsCount = list.size() - 1

                when (val head = list[0]) {
                    is Callable -> head.call(ctx,  Array(argsCount) { i -> list[i + 1].eval(ctx) })
                    is FunctionSpec -> evalFunction(
                        ctx,
                        head,
                        Array(argsCount) { i -> list[i + 1] }) // .eval(ctx)
                    is FunctionSymbol -> evalFunction(
                        ctx,
                        head.functionSpec,
                        Array(argsCount) { i -> list[i + 1] }) // .eval(ctx)
                    is MacroSpec -> evalMacro(
                        ctx,
                        head,
                        Array(argsCount) { i -> list[i + 1] }) // .eval(ctx)
                    is MacroSymbol -> evalMacro(
                        ctx,
                        head.macroSpec,
                        Array(argsCount) { i -> list[i + 1] }) // .eval(ctx)
                    is ReflectionSymbol -> invoke(
                        ctx,
                        head,
                        Array(argsCount) { i -> list[i + 1].eval(ctx) })
                    is Symbol -> {
                        when (val value = head.get(ctx)) {
                            is Callable -> value.call(ctx, Array(argsCount) { i -> list[i + 1].eval(ctx) })
                            is Null -> throw UninitializedSymbolAccessException(ctx, head)
                            else -> throw NotCallableException(ctx, value)
                        }
                    }
                    else -> this
                }
            }
            list.isEmpty() -> Null
            else -> throw IllegalStateException()
        }
    }

    fun eval(ctx: Ctx, head: Any, args: Array<Any>): Any =
        when (head) {
            is FunctionSpec -> evalFunction(ctx, head, args)
            is MacroSpec -> evalMacro(ctx, head, args)
            is ReflectionSymbol -> invoke(ctx, head, args)
            is Callable -> head.call(ctx, args)
            is FunctionObject -> head.call(ctx, Array(args.size) { index -> args[index + 1].eval(ctx) })
            else ->  throw NotCallableException(ctx, head)
        }

    fun invoke(ctx: Ctx, name: Symbol, args: Array<Any>): Any {
        val invokee = args[0].eval(ctx)
        val methods = arrayListOf<Method>()
        val methodName = name.name.substring(1)
        val argsCount = args.size - 1
        val invokeeClass = invokee::class.java
        val objectMethods = invokeeClass.methods

        for (i in objectMethods.indices)
            if (objectMethods[i].name == methodName) {
                if (objectMethods[i].parameterTypes.size == argsCount)
                    methods.add(objectMethods[i])
            }

        return if (methods.isNotEmpty()) {
            val evaluatedArgs = arrayListOf<Any>()

            for (i in 1 until argsCount)
                evaluatedArgs[i] = args[i].eval(ctx)

            for (method in methods) {
                val parameterTypes = method.parameterTypes
                val parameters = arrayOfNulls<Any>(argsCount)

                try {
                    for (i in 0 until argsCount) {
                        parameters[i] = when (parameterTypes[i]) {
                            Int::class -> (evaluatedArgs[i] as Long).toInt()
                            Float::class -> (evaluatedArgs[i] as Double).toFloat()
                            else -> evaluatedArgs[i]
                        }
                    }

                    return if (argsCount > 0) method.invoke(invokee, *parameters)!! else method.invoke(invokee) ?: Null
                } catch (e: Exception) {
                    Log.e("FlxVM", "Failed to invoke callable assigned to symbol $name. Exception: ${e.message}" )
                }
            }
            Null
        } else
            throw MethodNotFoundException(ctx, invokeeClass, methodName)
    }

    fun evalFunction(ctx: Ctx, spec: FunctionFormSpec, args: Array<Any>): Any {
        val values = Array(args.size) { i -> args[i].eval(ctx) }
        val signature = spec.checkArgs(values) ?: throw InvalidArgTypeException(ctx, spec, spec.getArgError(ctx, values))
        return spec.function(ctx, values, signature.index)
    }

    fun evalMacro(ctx: Ctx, spec: FunctionFormSpec, args: Array<Any>): Any {
        val signature = spec.checkArgs(args) ?: throw InvalidArgTypeException(ctx, spec, spec.getArgError(ctx, args))
        return spec.function(ctx, args, signature.index)
    }
}