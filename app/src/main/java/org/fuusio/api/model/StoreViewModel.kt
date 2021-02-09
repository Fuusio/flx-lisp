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

import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.ViewModel
import org.fuusio.api.redux.Action
import org.fuusio.api.redux.Subscription
import org.koin.core.KoinComponent

@Suppress("LeakingThis")
abstract class StoreViewModel<T: ModelState> : ViewModel(), LifecycleObserver, KoinComponent {

    protected var modelManager: ModelManager<T> = createModelManager()
    private val subscription: Subscription<T> = modelManager.getSubscriber()(::modelObserver)

    protected abstract fun createModelManager(model: T? = null): ModelManager<T>

    private fun modelObserver(changedModel: T, action: Action) {
        onModelReduced(changedModel, action)
    }

    protected abstract fun onModelReduced(model: T, action: Action)

    @Suppress("unused")
    fun setInitialModel(model: T) {
        modelManager.setInitialModel(model)
    }

    fun dispose() {
        subscription.unsubscriber(::onModelReduced)
        onDispose()
    }

    fun dispatch(action: Action) {
        subscription.dispatcher(action)
    }

    fun canRedo() = modelManager.canRedo()

    fun canUndo() = modelManager.canUndo()

    fun redo() = modelManager.redo()

    fun undo() = modelManager.undo()

    open fun onDispose() {}
}