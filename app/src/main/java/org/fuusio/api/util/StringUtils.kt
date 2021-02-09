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
package org.fuusio.api.util

object StringUtil {

    fun isValidIdentifier(string: String): Boolean {
        if (string.isNotBlank()) {
            if (!string[0].isJavaIdentifierStart()) return false

            for (i in 1 until string.length) {
                if (!string[i].isJavaIdentifierPart()) return false
            }
            return true
        }
        return false
    }

    fun toName(resourceName: String): String {
        val index = resourceName.lastIndexOf('/') + 1
        val simpleName = if (index > 0) resourceName.substring(index) else resourceName
        val builder = StringBuilder()
        var capitalizeNext = false
        builder.append(simpleName[0].toUpperCase())

        for (i in 1 until simpleName.length) {
            when (val char = simpleName[i]) {
                '_' -> {
                    capitalizeNext = true
                    builder.append(' ')
                }
                else -> if (capitalizeNext) {
                    builder.append(char.toUpperCase())
                    capitalizeNext = false
                } else {
                    builder.append(char)
                }
            }
        }
        return builder.toString()
    }
}