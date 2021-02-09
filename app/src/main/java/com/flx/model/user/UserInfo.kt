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
package com.flx.model.user

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import org.fuusio.flx.core.util.Literals

@Entity(tableName = "users")
data class UserInfo(
    @PrimaryKey @ColumnInfo(name = "uid") val uid: Long = DEFAULT_USER_UID,
    @ColumnInfo(name = "first_name") val firstName: String = Literals.EMPTY_STRING,
    @ColumnInfo(name = "last_name") val lastName: String = Literals.EMPTY_STRING,
    @ColumnInfo(name = "user_name") val userName: String = firstName,
    @ColumnInfo(name = "email") val email: String = Literals.EMPTY_STRING,
    @ColumnInfo(name = "eula_accepted") val eulaAccepted: Boolean = false,
) {
    companion object {
        const val PREFERENCE_EULA_ACCEPTED = "key.eula.accepted"
        const val DEFAULT_USER_UID: Long = 0xC0DE
    }
}
