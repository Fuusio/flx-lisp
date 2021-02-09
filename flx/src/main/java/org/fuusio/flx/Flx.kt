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
package org.fuusio.flx

import org.fuusio.flx.core.*
import org.fuusio.flx.core.function.FunctionSpec
import org.fuusio.flx.core.function.FunctionSymbol
import org.fuusio.flx.core.logger.Logger
import org.fuusio.flx.core.macro.MacroSpec
import org.fuusio.flx.core.macro.MacroSymbol
import org.fuusio.flx.core.reflection.ReflectionSymbol
import org.fuusio.flx.core.repl.FlxRepl
import org.fuusio.flx.core.vm.Ctx

object Flx {

    var logger = Logger()

    private var rootContext: Ctx? = null

    var ctx: Ctx
        get() = rootContext!!
        set(value) {
            val shouldInternSymbols = rootContext == null
            rootContext = value
            if (shouldInternSymbols) internSymbols()
        }

    private val keywords = hashMapOf<String, Keyword>()
    private val constantSymbols = hashMapOf<String, ConstantSymbol>()
    private val functionSymbols = hashMapOf<String, FunctionSymbol>()
    private val macroSymbols = hashMapOf<String, MacroSymbol>()
    private val symbols = hashMapOf<String, Symbol>()

    private fun internSymbols() {
        internConstantSymbols()
        FunctionSpec.internSymbols()
        MacroSpec.internSymbols()
    }

    fun reset() {
        ctx.reset()
        keywords.clear()
        symbols.clear()
    }

    fun finalize() {
        rootContext = null
    }

    fun eval(string: String) = FlxRepl().eval(string)

    fun getKeyword(string: String) = keywords[string] ?: createKeyword(string)

    private fun createKeyword(string: String): Keyword {
        val keyword = Keyword.create(string)
        keywords[string] = keyword
        return keyword
    }

    fun getSymbol(spec: FunctionFormSpec): Symbol
            = when {
                spec.isMacroSpec() -> macroSymbols[spec.symbol] ?: throw IllegalStateException("Failed to find symbol: ${spec.symbol}")
                else -> functionSymbols[spec.symbol]
                    ?: throw IllegalStateException("Failed to find symbol: ${spec.symbol}")
            }

    fun getSymbol(macroSpec: MacroSpec) = macroSymbols[macroSpec.symbol]!!

    fun getSymbol(string: String): Symbol =
        functionSymbols[string] ?: (macroSymbols[string] ?: (constantSymbols[string] ?: (symbols[string] ?: internSymbol(string))))

    private fun internSymbol(string: String): Symbol {
        val symbol = if (string.startsWith('.'))
            ReflectionSymbol(string)
        else
            Symbol(string)
        internSymbol(symbol)
        return symbol
    }

    fun internSymbol(symbol: Symbol): Symbol {
        when (symbol) {
            is FunctionSymbol -> functionSymbols[symbol.name] = symbol
            is MacroSymbol -> macroSymbols[symbol.name] = symbol
            is ConstantSymbol -> constantSymbols[symbol.name] = symbol
            else -> symbols[symbol.name] = symbol
        }
        return symbol
    }

    fun isInitialized() = rootContext != null

    private fun internConstantSymbols() {
        internConstantSymbol(NAME_E, Math.E)
        internConstantSymbol(NAME_PI, Math.PI)
    }

    private fun internConstantSymbol(name: String, value: Any) {
        internSymbol(ConstantSymbol(name, value))
    }
}