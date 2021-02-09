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

typealias ActionDispatcher = (action: Action) -> Unit

typealias ActionActor<State> = (store: Store<State>, state: State?, action: Action) -> Action

typealias StateGetter<State> = () -> State?

typealias Reducer<State> = (state: State, action: Action) -> State

typealias StateObserver<State> = (state: State, action: Action) -> Unit

typealias Subscriber<State> = (observer: StateObserver<State>) -> Subscription<State>

typealias Unsubscriber<State> = (observer: StateObserver<State>) -> Boolean