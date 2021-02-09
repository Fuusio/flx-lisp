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
package com.flx.features.repl.logic

import android.app.Activity
import org.fuusio.flx.Flx
import org.fuusio.flx.Program
import org.fuusio.flx.core.vm.*

class ExecutionManager {

    fun setupExecutionContext(
        activity: Activity,
        vmObserver: VmObserver,
        exceptionObserver: ExceptionObserver
    ): AndroidCtx {
        val flxCtx = FlxCtx(activity)
        flxCtx.program = Program()
        val androidCtx = AndroidCtx(parent = flxCtx)
        val vm = FlxScreenVM(androidCtx)
        vm.addObserver(vmObserver)
        Flx.reset()
        Flx.ctx = flxCtx
        flxCtx.exceptionObserver = exceptionObserver
        flxCtx.vm = vm
        androidCtx.vm = vm
        return androidCtx
    }
}