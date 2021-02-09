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
package org.fuusio.api.model

import org.fuusio.api.redux.*
import org.fuusio.api.util.Stack

open class DefaultModelManager<T: ModelState>(
    model: T? = null,
    reducers: List<Reducer<T>> = listOf(),
    actors: List<ActionActor<T>> = listOf(),
    private val maxUndoCount: Int = UNDO_DISABLED_COUNT
) : DefaultStore<T>(model, reducers, actors), ModelManager<T> {

    private val undoStack = Stack<ModelChangeEvent<T>>(maxUndoCount)

    private var undoneModelChangeEvent: ModelChangeEvent<T>? = null

    override fun setInitialModel(model: T) {
        setInitialState(model)
    }

    override fun setState(state: T, action: Action) {
        val oldState = getState()
        if (oldState != null && action !is NoopAction && maxUndoCount > UNDO_DISABLED_COUNT )
            undoStack.push(ModelChangeEvent(oldState, action))
        super.setState(state, action)
    }

    override fun canUndo() = undoStack.capacity > UNDO_DISABLED_COUNT && undoStack.isNotEmpty()

    override fun canRedo() = undoneModelChangeEvent != null

    override fun redo() {
        dispatch(undoneModelChangeEvent!!.action)
        undoneModelChangeEvent = null
    }

    override fun undo() {
        val event = undoStack.pop()!!
        undoneModelChangeEvent = event
        setState(event.model, NoopAction)
    }

    companion object {
        const val UNDO_DISABLED_COUNT = 0
    }
}