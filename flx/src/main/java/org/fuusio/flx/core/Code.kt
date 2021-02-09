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

import org.fuusio.flx.core.error.FlxException
import org.fuusio.flx.core.error.FlxRuntimeException
import org.fuusio.flx.core.util.Literals
import org.fuusio.flx.core.vm.Ctx
import java.lang.StringBuilder

open class Code(private val forms: Array<Any>) : EvaluableObject() {

    var name: String = Literals.EMPTY_STRING

    override fun eval(ctx: Ctx): Any {
        var value: Any = Null

        try {
            forms.forEach { form ->
                if (ctx.canContinue()) value = form.eval(ctx) else return Null
            }
        } catch (exception: FlxException) {
            ctx.onException(exception)
        } catch (exception: Exception) {
            ctx.onException(FlxRuntimeException(ctx, exception))
        }
        return value
    }

    override fun toString(): String {
        val string = StringBuilder()
        forms.forEach { form ->
            if (string.isNotEmpty()) string.append(' ')
            string.append(form.toLiteral())
        }
        return "(code $string)"
    }
}