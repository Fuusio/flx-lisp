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

import android.os.Handler
import android.os.Looper
import org.fuusio.flx.core.util.Literals
import org.fuusio.flx.core.vm.Ctx
import java.util.*
import kotlin.concurrent.fixedRateTimer

class PeriodicTimerObservable(ctx: Ctx, private val initialDelay: Long = 0L, private val period: Long = 1000L)
    : Observable(ctx) {

    private var elapsedTime = 0L

    private lateinit var timer: Timer

    override fun doStart(): Boolean {
        startTimer()
        return true
    }

    override fun doPause() {}

    override fun doResume() {}

    override fun doStop() {
        stopTimer()
    }

    override fun getErrorDescription() = Literals.EMPTY_STRING

    private fun startTimer() {
        val handler = Handler(Looper.getMainLooper())

        timer = fixedRateTimer(period = period, initialDelay = initialDelay, name = "TimeObservable-Timer-${index++}") {

            if (executionState.isStarted()) {
                val timeNow = System.currentTimeMillis()
                elapsedTime += period
                handler.post {  dispatch(ctx, arrayOf(timeNow, elapsedTime)) }
            }
        }
    }

    private fun stopTimer() {
        timer.cancel()
        timer.purge()
    }

    companion object {
        private var index = 0
    }

    override fun toString(): String =
        "PeriodicTimerObservable [ initialDelay = $initialDelay, period = $period ]"
}