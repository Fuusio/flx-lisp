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
package org.fuusio.flx.core.rx

import org.fuusio.flx.core.function.FunctionObject
import org.fuusio.flx.core.vm.*
import kotlin.coroutines.Continuation

abstract class Observable(protected val ctx: Ctx) : VmObserver {

    protected var errorMessage = "Unknown error"
    protected var executionState: ExecutionState = ExecutionState.IDLE
    protected lateinit var subscription: Subscription

    private val observerFunctions = mutableListOf<FunctionObject>()

    fun subscribe(observer: FunctionObject): Subscription {
        observerFunctions.add(observer)
        subscription = Subscription(this, observer)
        return subscription
    }

    fun unsubscribe(subscription: Subscription) {
        observerFunctions.remove(subscription.observer)
    }

    fun dispatch(ctx: Ctx, eventArgs: Array<Any>) {
        observerFunctions.forEach { function -> function.call(ctx, eventArgs) }
    }

    fun start(): Boolean =
        when (executionState) {
            ExecutionState.IDLE -> {
                ctx.getFlxVm().addObserver(this)
                executionState = ExecutionState.STARTED
                doStart()
            }
            else -> { false }
        }

    fun pause() {
        when (executionState) {
            ExecutionState.STARTED -> {
                doPause()
                executionState = ExecutionState.PAUSED
            }
            else -> {}
        }
    }

    fun resume() {
        when (executionState) {
            ExecutionState.PAUSED -> {
                doResume()
                executionState = ExecutionState.STARTED
            }
            else -> {}
        }
    }

    fun stop() {
        when (executionState) {
            ExecutionState.STARTED, ExecutionState.PAUSED -> {
                val vm = ctx.getFlxVm()
                vm.removeObserver(this)
                doStop()
                vm.dispose()
                executionState = ExecutionState.STOPPED
            }
            else -> {}
        }
    }

    abstract fun doStart(): Boolean

    abstract fun doPause()

    abstract fun doResume()

    abstract fun doStop()

    override fun onStarted(vm: FlxVM) {}

    override fun onPaused(vm: FlxVM) {
        pause()
    }

    override fun onResumed(vm: FlxVM) {
        resume()
    }

    override fun onStopped(vm: FlxVM) {
        stop()
        ctx.vm?.removeObserver(this)
        ctx.vm?.dispose()
    }

    open fun getErrorDescription(): String = errorMessage
}