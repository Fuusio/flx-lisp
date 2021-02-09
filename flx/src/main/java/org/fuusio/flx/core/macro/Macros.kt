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
@file:Suppress("UNUSED_PARAMETER")

package org.fuusio.flx.core.macro

import bsh.Interpreter
import org.fuusio.flx.Flx
import org.fuusio.flx.core.*
import org.fuusio.flx.core.error.ArgError
import org.fuusio.flx.core.error.InvalidArgException
import org.fuusio.flx.core.error.ValRedefinitionException
import org.fuusio.flx.core.error.VarRedefinitionException
import org.fuusio.flx.core.function.DefinedFunction
import org.fuusio.flx.core.function.FunctionObject
import org.fuusio.flx.core.function.FunctionSpec
import org.fuusio.flx.core.function.FunctionSymbol
import org.fuusio.flx.core.type.Array
import org.fuusio.flx.core.type.CoreType
import org.fuusio.flx.core.type.List
import org.fuusio.flx.core.type.Sequence
import org.fuusio.flx.core.type.impl.ImmutableList
import org.fuusio.flx.core.util.isEven
import org.fuusio.flx.core.util.isOdd
import org.fuusio.flx.core.vm.Ctx

fun fnAssign(ctx: Ctx, args: kotlin.Array<Any>, signatureIndex: Int): Any {
    val symbol = args[0] as Symbol
    val value = args[1].eval(ctx)
    when (val assignment = ctx.getAssignment(symbol)) {
        is ValAssignment -> throw ValRedefinitionException(ctx, symbol)
        is VarAssignment -> assignment.set(value)
        is SymbolAssignment -> assignment.set(value)
        else -> ctx.addAssignment(symbol, SymbolAssignment(value))
    }
    return value
}

fun fnSwitch(ctx: Ctx, args: kotlin.Array<Any>, signatureIndex: Int): Any {
    val switchValue = args[0].eval(ctx)
    val argsCount = args.size
    val isEvenArgs = argsCount.isEven()
    val lastIndex = argsCount - if (isEvenArgs) 0 else 1
    for (i in 1 until lastIndex step 2) {
        val caseValue = args[i].eval(ctx)
        if (equals(caseValue, switchValue) || caseValue is Else) return args[i + 1].eval(ctx)
    }
    return Null
}

private fun equals(value1: Any, value2: Any): Boolean =
    normalize(value1) == normalize(value2)

private fun normalize(value: Any): Any =
    when (value) {
        is Int -> value.toLong()
        is Short -> value.toLong()
        is Byte -> value.toLong()
        is Float -> value.toDouble()
        else -> value
    }

fun fnDecrease(ctx: Ctx, args: kotlin.Array<Any>, signatureIndex: Int): Number {
    val symbol = args[0] as Symbol
    val value: Number = when (val originalValue = symbol.get(ctx)) {
        is Float -> originalValue.toDouble() - 1
        is Double -> originalValue - 1
        is Number -> originalValue.toLong() - 1
        else -> throw InvalidArgException(
            ctx,
            Flx.getSymbol(MacroSpec.DECREASE),
            1,
            NAME_VARIABLE,
            "Symbol is required to have a number value"
        )
    }
    symbol.set(ctx, value)
    return value
}

fun fnFn(ctx: Ctx, args: kotlin.Array<Any>, signatureIndex: Int): DefinedFunction {
    val paramsArray = args[0] as Array
    val params = Array(paramsArray.size()) { i -> getSymbol(
        ctx,
        MacroSpec.FUN,
        0,
        NAME_ARGS,
        paramsArray[i]
    )}
    val body = args[1]
    return DefinedFunction(null, params, body)
}

fun fnFun(ctx: Ctx, args: kotlin.Array<Any>, signatureIndex: Int): Symbol {
    val name = args[0] as Symbol
    val paramsArray = args[1] as Array
    val params = Array(paramsArray.size()) { i -> getSymbol(
        ctx,
        MacroSpec.FUN,
        1,
        NAME_ARGS,
        paramsArray[i]
    )}
    val body = when (val arg = args[2]) {
        is List -> {
            val head = arg.first()
            if (head is FunctionSymbol && head.functionSpec == FunctionSpec.COMPILE) {
                ParsedForm(arg[1].toString())
            } else {
                arg
            }
        }
        else -> arg
    }
    val function = DefinedFunction(name, params, body)
    ctx.set(name, function)
    return name
}

fun fnBeanshell(ctx: Ctx, args: kotlin.Array<Any>, signatureIndex: Int): Any {
    val script = args[0].eval(ctx).toString()
    val interpreter = Interpreter()
    for (i in 1 until args.size) {
        val symbol = args[i] as Symbol
        interpreter.set(symbol.name, ctx.get(symbol))
    }
    val result = interpreter.eval(script)
    for (i in 1 until args.size) {
        val symbol = args[i] as Symbol
        symbol.set(ctx, interpreter.get(symbol.name))
    }
    return result ?: Null
}

fun fnCallback(ctx: Ctx, args: kotlin.Array<Any>, signatureIndex: Int): Callback {
    val code = when (val arg = args[0]) {
        is Code -> arg
        else -> arg.eval(ctx) as Code
    }
    val params = Array(args.size - 1) { i -> getSymbol(
        ctx,
        MacroSpec.CALLBACK,
        i + 1,
        NAME_ARGS,
        args[i + 1]
    )}
    return Callback(params, code)
}

fun fnProc(ctx: Ctx, args: kotlin.Array<Any>, signatureIndex: Int): Callback {
    val paramsArray = args[0] as Array
    val params = Array(paramsArray.size()) { i -> getSymbol(
        ctx,
        MacroSpec.PROC,
        0,
        NAME_ARGS,
        paramsArray[i]
    )}
    val code = args[1] as Code
    return Callback(params, code)
}

fun fnCode(ctx: Ctx, args: kotlin.Array<Any>, signatureIndex: Int): Code {
    val forms = mutableListOf<Any>()
    args.forEach { arg -> forms.add(arg) }
    return Code(forms.toTypedArray())
}

fun fnDo(ctx: Ctx, args: kotlin.Array<Any>, signatureIndex: Int): Any {
    var result: Any = Null
    args.forEach { arg -> result = arg.eval(ctx) }
    return result
}

fun fnRepeat(ctx: Ctx, args: kotlin.Array<Any>, signatureIndex: Int): Any {
    val times = when (val number = args[0].eval(ctx)) {
        is Number -> number.toLong()
        else -> throw InvalidArgException(
            ctx, MacroSpec.REPEAT, ArgError(
                0,
                "n",
                CoreType.NUMBER,
                number::class
            ), "First argument has to evaluate to a number"
        )
    }
    val form = args[1]
    var result: Any = Null
    for (i in 0 until times) {
        result = form.eval(ctx)
        if (!ctx.canContinue()) break
    }
    return result
}

fun fnForEach(ctx: Ctx, args: kotlin.Array<Any>, signatureIndex: Int): Any {
    val macroCtx = ctx.create()
    val symbol = args[0] as Symbol
    val form = args[2]
    var result: Any = Null

    when (val sequence = args[1].eval(macroCtx)) {
        is Sequence -> {
            val count = sequence.size()

            for (i in 0 until count) {
                symbol.set(macroCtx, sequence[i])
                result = form.eval(macroCtx)
                if (!macroCtx.canContinue()) break
            }
        }
        is String -> {
            sequence.forEach { char ->
                symbol.set(macroCtx, char)
                result = form.eval(macroCtx)
                if (!macroCtx.canContinue()) return result
            }
        }
        else -> throw InvalidArgException(
            ctx, MacroSpec.FOR_EACH, ArgError(
                1,
                "sequence",
                CoreType.SEQUENCE,
                sequence::class
            ), "Second argument has to evaluate to a sequence"
        )
    }
    return result
}

@Suppress("UNUSED_PARAMETER")
fun fnHelp(ctx: Ctx, args: kotlin.Array<Any>, signatureIndex: Int): Info {
    val systemSymbol = args[0] as SystemSymbol
    val name = systemSymbol.name
    val spec = systemSymbol.getSpec()
    val builder = StringBuilder()

    spec.signatureSpecs.forEach { signatureSpec ->
        if (builder.isEmpty()) builder.append(' ')

        signatureSpec.argSpecs.forEach { argSpec ->
            builder.append(' ')
            builder.append(argSpec.getArgName())
            builder.append(": ")
            builder.append(argSpec.type.getTypeName())
        }
    }

    val argNames = builder.toString()
    return Info("($name$argNames) \u21e8 ${spec.outputType.getTypeName()}")
}

fun fnIncrease(ctx: Ctx, args: kotlin.Array<Any>, signatureIndex: Int): Number {
    val symbol = args[0] as Symbol
    val value: Number = when (val originalValue = symbol.get(ctx)) {
        is Float -> originalValue.toDouble() + 1
        is Double -> originalValue + 1
        is Number -> originalValue.toLong() + 1
        else -> throw InvalidArgException(
            ctx,
            Flx.getSymbol(MacroSpec.INCREASE),
            1,
            NAME_VARIABLE,
            "Symbol is required to have a number value"
        )
    }
    symbol.set(ctx, value)
    return value
}

fun fnIf(ctx: Ctx, args: kotlin.Array<Any>, signatureIndex: Int): Any
        = if (args[0].eval(ctx).toBoolean()) args[1].eval(ctx) else args[2].eval(ctx)

fun fnLet(ctx: Ctx, args: kotlin.Array<Any>, signatureIndex: Int): Any {
    val bindings = when (val array = args[0]) {
        is Array -> array
        else -> {
            val argError = ArgError(0, NAME_BINDINGS, CoreType.ARRAY, array::class)
            throw InvalidArgException(
                ctx,
                MacroSpec.LET,
                argError,
                "Expression ${args[0].toLiteral()} does not evaluate to an Array"
            )
        }
    }

    val bindingsCount = bindings.size()

    if (bindingsCount == 0 || bindingsCount.isOdd()) {
        val argError = ArgError(0, NAME_BINDINGS, CoreType.ANY, Any::class)
        throw InvalidArgException(ctx, MacroSpec.LET, argError, "Invalid bindings definition")
    }

    val letCtx = ctx.create()

    for (i in 0 until bindingsCount step 2) {
        when (val symbol = bindings[i]) {
            is Symbol -> letCtx.set(symbol, bindings[i + 1].eval(ctx))
            else -> {
                val argError = ArgError(0, NAME_BINDINGS, CoreType.SYMBOL, symbol::class)
                throw InvalidArgException(
                    ctx,
                    MacroSpec.LET,
                    argError,
                    "Invalid symbol bindings definition. Expression ${symbol.toLiteral()} is not a Symbol"
                )
            }
        }
    }

    var value: Any = Null

    for (i in 1 until args.size) {
        value = args[i].eval(letCtx)
    }
    return value
}

fun fnMap(ctx: Ctx, args: kotlin.Array<Any>, signatureIndex: Int): List {
    val function = when (val head = args[0].eval(ctx)) {
        is FunctionObject -> head
        is FunctionSymbol -> head.functionSpec
        is MacroSymbol -> head.macroSpec
        else -> {
            val argError = ArgError(0, NAME_FUNCTION, CoreType.FUNCTION, head::class)
            throw InvalidArgException(
                ctx,
                MacroSpec.MAP,
                argError,
                "Expression ${args[0].toLiteral()} does not reference to a function"
            )
        }
    }

    val items = mutableListOf<Any>()
    val sequences = mutableListOf<Any>()

    for (i in 1 until args.size) {
        when (val sequence = args[i].eval(ctx)) {
            is Sequence -> sequences.add(sequence)
            is String -> sequences.add(sequence)
            else -> {
                val argError = ArgError(0, NAME_SEQUENCE, CoreType.SEQUENCE, sequence::class)
                throw InvalidArgException(
                    ctx,
                    MacroSpec.MAP,
                    argError,
                    "Invalid sequence argument. Expression ${args[1].toLiteral()} is not a sequence nor string"
                )
            }
        }
    }

    val sequencesCount = sequences.size
    var itemIndex = 0

    loop@ while (true) {
        val arguments = mutableListOf<Any>()
        for (i in 0 until sequencesCount) {
            when (val sequence = sequences[i]) {
                is Sequence -> {
                    if (itemIndex >= sequence.size()) {
                        break@loop
                    } else {
                        arguments.add(sequence[itemIndex])
                    }
                }
                is String -> {
                    if (itemIndex >= sequence.length) {
                        break@loop
                    } else {
                        arguments.add(sequence[itemIndex])
                    }
                }
                else -> {}
            }
        }
        items.add(function.call(ctx, arguments.toTypedArray()))
        itemIndex++
    }
    return ImmutableList.create(items)
}

@Suppress("UNUSED_PARAMETER")
fun fnQuote(ctx: Ctx, args: kotlin.Array<Any>, signatureIndex: Int): Any = args[0]

fun fnVal(ctx: Ctx, args: kotlin.Array<Any>, signatureIndex: Int): Symbol {
    val symbol = args[0] as Symbol

    if (ctx.hasAssignment(symbol)) throw ValRedefinitionException(ctx, symbol)

    val value = args[1].eval(ctx)
    ctx.addAssignment(symbol, ValAssignment(value))
    return symbol
}

fun fnVar(ctx: Ctx, args: kotlin.Array<Any>, signatureIndex: Int): Symbol {
    val symbol = args[0] as Symbol

    if (ctx.hasAssignment(symbol)) throw VarRedefinitionException(ctx, symbol)

    var value: Any = Null
    for (i in 1 until args.size) {
        value = args[1].eval(ctx)
    }
    ctx.addAssignment(symbol, VarAssignment(value))
    return symbol
}

fun fnWhen(ctx: Ctx, args: kotlin.Array<Any>, signatureIndex: Int): Any {
    val argsCount = args.size
    val isEvenArgs = argsCount.isEven()
    val lastIndex = argsCount - if (isEvenArgs) 0 else 1
    for (i in 0 until lastIndex step 2) {
        if (args[i].eval(ctx).toBoolean()) return args[i + 1].eval(ctx)
    }
    return Null
}

fun fnWhile(ctx: Ctx, args: kotlin.Array<Any>, signatureIndex: Int): Any {
    val expression = args[0]
    val codeBlock = args[1]
    var result: Any = Null
    while (expression.eval(ctx).toBoolean() && ctx.canContinue()) { result = codeBlock.eval(ctx) }
    return result
}

@Suppress("SameParameterValue")
fun getSymbol(ctx: Ctx, spec: MacroSpec, argIndex: Int, argName: String, arg: Any): Symbol {
    return when (arg) {
        is Symbol -> arg
        else -> when (spec) {
            MacroSpec.CALLBACK -> {
                val description = "Arg at index $argIndex should be a Symbol"
                throw InvalidArgException(
                    ctx,
                    spec,
                    ArgError(argIndex, argName, CoreType.SYMBOL, arg::class),
                    description
                )
            }
            MacroSpec.FUN -> {
                val description = "Args array should contains only Symbols"
                throw InvalidArgException(
                    ctx,
                    spec,
                    ArgError(argIndex, argName, CoreType.SYMBOL, arg::class),
                    description
                )
            }
            MacroSpec.PROC -> {
                val description = "Args array should contains only Symbols"
                throw InvalidArgException(
                    ctx,
                    spec,
                    ArgError(argIndex, argName, CoreType.SYMBOL, arg::class),
                    description
                )
            }
            else -> throw IllegalStateException()
        }
    }
}
