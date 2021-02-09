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
package com.flx.features.database.dao

import androidx.room.*
import com.flx.model.user.UserInfo

@Dao
interface UserInfoDao {

    suspend fun getUserInfo(): UserInfo {
        val userInfo = getById(UserInfo.DEFAULT_USER_UID)
        return userInfo ?: UserInfo()
    }

    @Query("SELECT * FROM users")
    suspend fun getAll(): List<UserInfo>

    @Query("SELECT * FROM users WHERE uid IN (:ids)")
    suspend fun getByIds(ids: LongArray): List<UserInfo>

    @Query("SELECT * FROM users WHERE uid = :id")
    suspend fun getById(id: Long): UserInfo?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(userInfo: UserInfo)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(vararg userInfos: UserInfo)

    @Delete
    suspend fun delete(userInfo: UserInfo)
}