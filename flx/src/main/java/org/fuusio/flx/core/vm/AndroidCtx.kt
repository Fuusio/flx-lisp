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
import android.content.res.Resources
import org.fuusio.flx.core.vm.Ctx
import org.fuusio.flx.core.vm.FlxVM

class AndroidCtx(
    vm: FlxVM? = null,
    parent: Ctx? = null
) : Ctx(vm, parent) {

    init {
        this.program = parent!!.program
    }

    fun getActivity(): Activity =
        when (parent) {
            is FlxCtx -> parent.activity!!
            is AndroidCtx -> parent.getActivity()
            else -> throw IllegalStateException()
        }

    override fun create(): Ctx {
        val childCtx = AndroidCtx(vm = vm, parent = this)
        childCtx.program = program
        return childCtx
    }

    override fun create(vm: FlxVM): Ctx =
        AndroidCtx(vm = vm, parent = this)

    fun getResources(): Resources = getActivity().resources!!

    override fun toast(ctx: Ctx, message: String) {
        parent?.toast(ctx, message)
    }

    companion object {

        private var nextId = 0xF00

        fun getNextViewId() = nextId++
    }
}