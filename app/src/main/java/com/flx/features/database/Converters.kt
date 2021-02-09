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
package com.flx.features.database

import androidx.room.TypeConverter
import java.lang.StringBuilder
import java.util.*

class DateConverter {

    @TypeConverter
    fun toDate(time: Long): Date = Date(time)

    @TypeConverter
    fun toTime(date: Date): Long = date.time
}

class StringListConverter {

    @TypeConverter
    fun toStringList(string: String): List<String> {
        val splitList = string.split(',', ' ')
        val list = mutableListOf<String>()
        splitList.forEach { splittedString ->
            val trimmedString = splittedString.trim()
            val normalizedString = if (trimmedString.contains("#")) trimmedString.replace("#", "") else trimmedString
            if (normalizedString.isNotBlank()) list.add(normalizedString)
        }
        return list
    }

    @TypeConverter
    fun toString(strings: List<String>): String {
        val builder = StringBuilder()
        strings.forEach { string ->
            val trimmedString = string.trim()
            if (trimmedString.isNotBlank()) {
                if (builder.isNotEmpty()) builder.append(',')
                builder.append(trimmedString)
            }
        }
        return builder.toString()
    }
}