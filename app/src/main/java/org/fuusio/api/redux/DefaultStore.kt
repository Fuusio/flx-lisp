/*
 * Copyright (C) 2001 - 2020 Marko Salmela
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
package org.fuusio.api.redux

import androidx.annotation.CallSuper

open class DefaultStore<T: State>(
    initialState: T? = null,
    private val reducers: List<Reducer<T>> = listOf(),
    private val actors: List<ActionActor<T>> = listOf()
) : Store<T> {

    private val observers = mutableSetOf<StateObserver<T>>()
    private var _state: T? = initialState

    override fun getSubscriber(): Subscriber<T> = ::subscribe

    protected open fun getState(): T? = _state

    protected open fun setState(state: T, action: Action) {
        _state = state
        onStateChanged(state, action)
    }

    override fun setInitialState(state: T) {
        if (_state == null) {
            setState(state, NoopAction)
        }
    }

    protected open fun dispatch(action: Action) {
        val reduceAction = applyActors(_state, action, actors)
        val state = _state

        if (state != null) {
            val changedState = applyReducers(state, reduceAction, reducers)

            if (changedState !== state) {
                setState(changedState, action)
            }
        }
    }

    protected open fun subscribe(observer: StateObserver<T>): Subscription<T> {
        observers.add(observer)
        _state?.let {state -> observer(state, NoopAction) }
        return Subscription(::dispatch, ::getState, ::unsubscribe)
    }

    protected open fun unsubscribe(observer: StateObserver<T>): Boolean = observers.remove(observer)

    private fun applyActors(
        state: T?,
        action: Action,
        actors: List<ActionActor<T>>
    ) : Action {
        var actorAction = action
        actors.forEach { actor -> actorAction = actor(this, state, actorAction) }
        return actorAction
    }

    private fun applyReducers(
        state: T,
        action: Action,
        reducers: List<Reducer<T>>
    ): T {
        var reducedState = state
        reducers.forEach { reducer -> reducedState = reducer(reducedState, action) }
        return reducedState
    }

    @CallSuper
    protected open fun onStateChanged(state: T, action: Action) {
        observers.forEach{ observer -> observer(state, action)}
    }
}