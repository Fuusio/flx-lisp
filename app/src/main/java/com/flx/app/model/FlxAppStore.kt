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
package com.flx.app.model

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.fuusio.api.redux.*
import com.flx.features.database.FlxDatabase
import com.flx.model.user.UserInfo
import org.koin.core.KoinComponent

class FlxAppStore(val db: FlxDatabase) : DefaultStore<FlxAppState>(
    getInitialState(),
    listOf(Companion::reduce),
    listOf(Companion::doDbAction)
), KoinComponent {

    fun saveUserInfo() {
        getState()?.let { state ->
            GlobalScope.launch {
                db.userInfoDao().insert(state.userInfo)
            }
        }
    }

    companion object {

        private fun getInitialState() = FlxAppState(UserInfo())

        @Suppress("UNUSED_PARAMETER")
        private fun doDbAction(store: Store<FlxAppState>, state: FlxAppState?, action: Action): Action =
            when (action) {
                is GetUserInfo -> {
                    GlobalScope.launch {
                        store as FlxAppStore
                        val userInfo = store.db.userInfoDao().getUserInfo()
                        store.dispatch(NotifyUserInfoUpdated(userInfo))
                    }
                    NoopAction
                }
                else -> action
            }

        private fun reduce(state: FlxAppState, action: Action): FlxAppState =
            when(action) {
                is NotifyUserInfoUpdated -> state.copy(userInfo = action.userInfo)
                is AcceptEula -> state.copy(userInfo = state.userInfo.copy(eulaAccepted = true))
                is DeclineEula -> state.copy(userInfo = state.userInfo.copy(eulaAccepted = false))
                else -> state
            }
    }
}