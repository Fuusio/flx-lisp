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
@file:Suppress("unused")

package org.fuusio.flx.core.macro

import org.fuusio.flx.Flx
import org.fuusio.flx.R
import org.fuusio.flx.core.*
import org.fuusio.flx.core.function.Function
import org.fuusio.flx.core.error.ArgError
import org.fuusio.flx.core.error.InvalidNumberOfArgsException
import org.fuusio.flx.core.function.FunctionSpec
import org.fuusio.flx.core.type.ArgModifier
import org.fuusio.flx.core.type.SignatureSpec
import org.fuusio.flx.core.type.CoreType
import org.fuusio.flx.core.type.CoreType.*
import org.fuusio.flx.core.vm.Ctx

enum class MacroSpec(
    override val symbol: String,
    override val function: Function,
    override val outputType: CoreType,
    override val signatureSpecs: Array<SignatureSpec>,
    override val style: FunctionStyle = FunctionStyle.LISP_FUNCTION,
    override val searchKeywords: String = "",
) : FunctionFormSpec
{
    HELP("?", ::fnHelp, INFO,
        signatures {
            signature {
                description(R.string.desc_help)
                arg(FUNCTION_FORM_SYMBOL, NAME_SYMBOL)
            }
        }
    ),

    ASSIGN("=", ::fnAssign, ANY,
        signatures {
            signature {
                description(R.string.desc_assign)
                arg(SYMBOL, NAME_VARIABLE)
                arg(ANY, NAME_ASSIGN_VALUE)
            }
        }
    ),

    BEANSHELL("beanshell", ::fnBeanshell, ANY,
        signatures {
            signature {
                description(R.string.desc_beanshell)
                arg(ANY, NAME_SCRIPT)
                arg(SYMBOL, NAME_VARIABLES, ArgModifier.VARARG)
            }
        }
    ),

    CALLBACK("callback", ::fnCallback, CoreType.CALLBACK,
        signatures {
            signature {
                description(R.string.desc_callback)
                arg(ANY, NAME_CODE)
                arg(SYMBOL, NAME_ARGS, ArgModifier.VARARG)
            }
        }
    ),

    DECREASE("--", ::fnDecrease, NUMBER,
        signatures {
            signature {
                description(R.string.desc_decrease)
                arg(SYMBOL, NAME_VARIABLE)
            }
        }
    ),

    SWITCH("switch", ::fnSwitch, ANY,
        signatures {
            signature {
                description(R.string.desc_switch)
                arg(ANY, NAME_SWITCH_VALUE)
                arg(ANY, NAME_CASE, ArgModifier.REPEATABLE)
                arg(ANY, NAME_FORM, ArgModifier.REPEATABLE)
            }
        }
    ),

    FN("fn", ::fnFn, FUNCTION,
        signatures {
            signature {
                description(R.string.desc_fn)
                arg(ARRAY, NAME_PARAMS)
                arg(ANY, NAME_BODY)
            }
        }
    ),

    FUN("fun", ::fnFun, SYMBOL,
        signatures {
            signature {
                description(R.string.desc_fun)
                arg(SYMBOL, NAME_NAME)
                arg(ARRAY, NAME_ARGS)
                arg(ANY, NAME_BODY)
            }
        }
    ),

    PROC("proc", ::fnProc, CoreType.CALLBACK,
        signatures {
            signature {
                description(R.string.desc_proc)
                arg(ARRAY, NAME_ARGS)
                arg(ANY, NAME_CODE)
            }
        }
    ),

    CODE("code", ::fnCode, CoreType.CODE,
        signatures {
            signature {
                description(R.string.desc_code)
                arg(ANY, NAME_FORMS, ArgModifier.VARARG)
            }
        }
    ),

    DO("do", ::fnDo, ANY,
        signatures {
            signature {
                description(R.string.desc_do)
                arg(ANY, NAME_FORMS, ArgModifier.VARARG)
            }
        }
    ),

//    FN("fn", ::fnFn, SYMBOL, R.string.desc_fn,
//        signatures(
//            signature(
//                arg(ARRAY, NAME_PARAMS),
//                arg(ANY, NAME_BODY)
//        ))
//    ),

//    FOR("for", ::fnFor, ANY,
//        signatures {
//            signature {
//                description(R.string.desc_for)
//                arg(ANY, NAME_N)
//                arg(ANY, NAME_DO_FORM)
//            }
//        }
//    ),

    REPEAT("repeat", ::fnRepeat, ANY,
        signatures {
            signature {
                description(R.string.desc_repeat)
                arg(ANY, NAME_N)
                arg(ANY, NAME_FORM)
            }
        }
    ),

    FOR_EACH("forEach", ::fnForEach, ANY,
        signatures {
            signature {
                description(R.string.desc_for_each)
                arg(SYMBOL, NAME_SYMBOL)
                arg(ANY, NAME_SEQUENCE)
                arg(ANY, NAME_FORM)
            }
        }
    ),

    ALIAS_HELP("help", ::fnHelp, INFO,
        signatures {
            signature {
                description(R.string.desc_help)
                arg(FUNCTION_FORM_SYMBOL, NAME_SYMBOL)
            }
        }
    ),

    IF("if", ::fnIf, ANY,
        signatures {
            signature {
                description(R.string.desc_if)
                arg(ANY, NAME_CONDITION)
                arg(ANY, NAME_THEN_FORM)
                arg(ANY, NAME_ELSE_FORM)
            }
        }
    ),

    INCREASE("++", ::fnIncrease, NUMBER,
        signatures {
            signature {
                description(R.string.desc_increase)
                arg(SYMBOL, NAME_VARIABLE)
            }
        }
    ),

    LET("let", ::fnLet, ANY,
        signatures {
            signature {
                description(R.string.desc_let)
                arg(ANY, NAME_BINDINGS)
                arg(ANY, NAME_FORMS, ArgModifier.VARARG)
            }
        }
    ),

    MAP("map", ::fnMap, LIST,
        signatures {
            signature {
                description(R.string.desc_map)
                arg(ANY, NAME_FUNCTION)
                arg(ANY, NAME_SEQUENCE)
                arg(ANY, NAME_SEQUENCES, ArgModifier.VARARG)
            }
        }
    ),

    QUOTE("quote", ::fnQuote, ANY,
        signatures {
            signature {
                description(R.string.desc_quote)
                arg(ANY, NAME_FORM)
            }
        }
    ),

    LAZY("lazy", ::fnQuote, ANY,
        signatures {
            signature {
                description(R.string.desc_lazy)
                arg(ANY, NAME_FORM)
            }
        }
    ),

    VAL("val", ::fnVal, SYMBOL,
        signatures {
            signature {
                description(R.string.desc_val)
                arg(SYMBOL, NAME_SYMBOL)
                arg(ANY, NAME_ASSIGN_VALUE)
            }
        }
    ),

    VAR("var", ::fnVar, SYMBOL,
        signatures {
            signature {
                description(R.string.desc_var)
                arg(SYMBOL, NAME_SYMBOL)
                arg(ANY, NAME_ASSIGN_VALUE_OPTIONAL, ArgModifier.OPTIONAL)
            }
        }
    ),

    WHEN("when", ::fnWhen, ANY,
        signatures {
            signature {
                description(R.string.desc_when)
                arg(ANY, NAME_CONDITION, ArgModifier.REPEATABLE)
                arg(ANY, NAME_FORM, ArgModifier.REPEATABLE)
            }
        }
    ),

    WHILE("while", ::fnWhile, ANY,
        signatures {
            signature {
                description(R.string.desc_while)
                arg(ANY, NAME_CONDITION)
                arg(ANY, NAME_DO_FORM)
            }
        }
    );

    override fun getId(): String = name

    override fun isMacroSpec(): Boolean = true

    override val functionName: Symbol?
        get() = Flx.getSymbol(symbol)

    override fun call(ctx: Ctx, args: Array<Any>): Any =
        Evaluator.evalMacro(ctx, this, args)

    override fun eval(ctx: Ctx): Any = this

    override fun hasArgs(): Boolean {
        signatureSpecs.forEach { signature -> if (signature.argSpecs.isNotEmpty()) return true }
        return false
    }

    override fun checkArgs(argValues: Array<Any>): SignatureSpec? {
        signatureSpecs.forEach { signatureSpec -> if (signatureSpec.checkArgs(argValues)) return signatureSpec }
        return null
    }

    override fun getArgError(ctx: Ctx, args: Array<Any>): ArgError {
        signatureSpecs.forEach { signatureSpec ->
            if (signatureSpec.checkArgsCount(args)) {
                val argError = signatureSpec.getArgError(args)
                if (argError != null) return argError
            }
        }
        throw InvalidNumberOfArgsException(ctx, symbol, args.size, signatureSpecs[0].getArgsCount())
    }

    companion object {

        fun internSymbols() {
            values().forEach { value ->
                val symbol = Flx.internSymbol(MacroSymbol(value))
                Flx.ctx.set(symbol, value)
            }
        }

        fun get(symbol: String, id: String? = null): FunctionFormSpec? {
            if (id != null) {
                values().forEach { value -> if (value.name == id) return@get value }
            } else {
                values().forEach { value -> if (value.symbol == symbol) return@get value }
            }
            return null
        }
    }
}