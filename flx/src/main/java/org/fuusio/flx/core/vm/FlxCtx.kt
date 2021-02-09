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
package org.fuusio.flx.core.vm

import android.app.Activity
import android.content.Context
import android.widget.Toast
import org.fuusio.flx.core.error.FlxException
import org.fuusio.flx.core.vm.Ctx
import org.fuusio.flx.core.vm.FlxVM


class FlxCtx(val context: Context) : Ctx(null, null) {

    val activity: Activity
        get() = context as Activity

    var exceptionObserver: ExceptionObserver? = null

    override fun create(): Ctx {
        val childCtx = AndroidCtx(vm = vm, parent = this)
        childCtx.program = program
        return childCtx
    }

    override fun create(vm: FlxVM): Ctx {
        val childCtx = AndroidCtx(vm = vm, parent = this)
        childCtx.program = program
        return childCtx
    }

    override fun toast(ctx: Ctx, message: String) {
        activity.let { context ->
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onException(exception: FlxException) {
        exceptionObserver?.onException(exception)
    }
}