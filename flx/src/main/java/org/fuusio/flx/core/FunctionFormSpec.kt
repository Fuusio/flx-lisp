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

import androidx.annotation.StringRes
import org.fuusio.flx.core.error.ArgError
import org.fuusio.flx.core.function.Function
import org.fuusio.flx.core.function.FunctionObject
import org.fuusio.flx.core.type.*
import org.fuusio.flx.core.vm.Ctx

interface FunctionFormSpec : FunctionObject {

    val function: Function
    val symbol: String
    val outputType: FlxType
    val searchKeywords: String
    val signatureSpecs: Array<SignatureSpec>
    val style: FunctionStyle

    fun getId(): String

    fun hasArgs(): Boolean

    fun isMacroSpec(): Boolean

    fun checkArgs(argValues: Array<Any>): SignatureSpec?

    fun getArgError(ctx: Ctx, args: Array<Any>): ArgError

    fun isGetterFunction(signature: SignatureSpec): Boolean =
        signature.argSpecs.size == 1 && symbol[0] == '.'

    fun isKotlinStyleFunction(): Boolean = style == FunctionStyle.KOTLIN_FUNCTION
}

fun arg(type: FlxType, name: String, argModifier: ArgModifier = ArgModifier.REQUIRED): ArgSpec
        = ArgSpec(type, name, argModifier)

fun signatures(
    init: MutableList<SignatureSpec>.() -> Unit): Array<SignatureSpec> {
    val signatureSpecs = mutableListOf<SignatureSpec>()
    signatureSpecs.init()
    var index = 0
    signatureSpecs.forEach { signatureSpec -> signatureSpec.index = index++ }
    return signatureSpecs.toTypedArray()
}

fun MutableList<SignatureSpec>.signature(init: SignatureSpecBuilder.() -> Unit): SignatureSpec {
    val builder = SignatureSpecBuilder()
    builder.init()
    val signatureSpec = builder.build()
    this.add(signatureSpec)
    return signatureSpec
}

fun signature(style: FunctionStyle = FunctionStyle.LISP_FUNCTION, @StringRes description: Int, vararg argSpecs: ArgSpec) = SignatureSpec(description, arrayOf(*argSpecs), 0, style)

enum class FunctionStyle {
    KOTLIN_FUNCTION,
    LISP_FUNCTION
}

data class SignatureSpecBuilder(
    @StringRes var descriptionResId: Int = 0,
    val argSpecs: MutableList<ArgSpec> = mutableListOf()
) {

    fun description(@StringRes descriptionResId: Int) {
        this.descriptionResId = descriptionResId
    }

    fun arg(type: FlxType, name: String, argModifier: ArgModifier = ArgModifier.REQUIRED): ArgSpec {
        val argSpec = ArgSpec(type, name, argModifier)
        argSpecs.add(argSpec)
        return argSpec
    }

    fun build(): SignatureSpec =
        SignatureSpec(descriptionResId, argSpecs.toTypedArray())
}