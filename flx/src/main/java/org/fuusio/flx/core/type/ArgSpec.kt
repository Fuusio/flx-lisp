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
package org.fuusio.flx.core.type

data class ArgSpec(
    val type: FlxType,
    val name: String,
    val argModifier: ArgModifier = ArgModifier.REQUIRED
) {

    fun getArgName(): String {
        val separatorIndex = name.indexOf(':')
        return if (separatorIndex >= 0) name.substring(separatorIndex + 1) else name
    }

    fun getArgLabel(): String? {
        val separatorIndex = name.indexOf(':')
        return if (separatorIndex >= 0) name.substring(0, separatorIndex) else null
    }

    fun isOptional() = argModifier.isOptional()

    fun isRepeatable() = argModifier.isRepeatable()

    fun isRequired() = argModifier.isRequired()

    fun isVarArg() = argModifier.isVararg()
}