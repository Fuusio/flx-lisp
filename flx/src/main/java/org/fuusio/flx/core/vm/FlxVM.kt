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

import org.fuusio.flx.Flx
import org.fuusio.flx.Program
import org.fuusio.flx.core.*
import org.fuusio.flx.core.repl.FlxRepl

open class FlxVM(val ctx: Ctx) {
    val repl = FlxRepl(ctx)
    var out = System.out!!

    var executionState = ExecutionState.IDLE

    private val observers = mutableSetOf<VmObserver>()

    fun addObserver(observer: VmObserver) {
        synchronized(observers) {
            observers.add(observer)
        }
    }

    fun removeObserver(observer: VmObserver) {
        synchronized(observers) {
            observers.remove(observer)
        }
    }

    open fun dispose() {}

    fun eval(expression: String): Any = repl.eval(expression)

    fun eval(form: Any): Any = form.eval(ctx)

    fun canContinue(): Boolean = executionState.isStarted()

    fun print(string: String) {
        out.print(string)
    }

    fun println(string: String) {
        out.println(string)
    }

    fun start(): ExecutionState {
        if (executionState == ExecutionState.IDLE) {
            executionState = ExecutionState.STARTED
            observers.forEach { observer -> observer.onStarted(this) }
        }
        return executionState
    }

    fun pause(): ExecutionState {
        if (executionState == ExecutionState.STARTED) {
            executionState = ExecutionState.PAUSED

            synchronized(observers) {
                observers.forEach { observer -> observer.onPaused(this) }
            }
        }
        return executionState
    }

    fun resume(): ExecutionState {
        if (executionState == ExecutionState.PAUSED) {
            executionState = ExecutionState.STARTED
            observers.forEach { observer -> observer.onResumed(this) }
        }
        return executionState
    }

    fun stop(): ExecutionState {
        if (executionState == ExecutionState.STARTED ||
            executionState == ExecutionState.PAUSED) {
            executionState = ExecutionState.STOPPED
            observers.forEach { observer -> observer.onStopped(this) }
        }
        return executionState
    }

    fun canPause() = executionState == ExecutionState.STARTED

    fun canResume() = executionState == ExecutionState.PAUSED

    fun canStart() = executionState == ExecutionState.IDLE

    fun canStop() = executionState == ExecutionState.STARTED || executionState == ExecutionState.PAUSED

    fun isStopped() = executionState == ExecutionState.STOPPED

    fun isPaused() = executionState == ExecutionState.PAUSED

    fun reset() {
        executionState = ExecutionState.IDLE
        ctx.reset()
        Flx.reset()
    }

    companion object {

        fun create(ctx: Ctx, program: Program = Program()): FlxVM {
            val vm = FlxVM(ctx)
            vm.ctx.program = program
            return vm
        }
    }
}