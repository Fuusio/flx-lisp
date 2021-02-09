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

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.flx.features.database.dao.UserInfoDao
import com.flx.model.user.UserInfo

@Database(entities = [UserInfo::class], version = 1, exportSchema = false)
@TypeConverters(DateConverter::class, StringListConverter::class)
abstract class FlxDatabase : RoomDatabase() {

    abstract fun userInfoDao(): UserInfoDao

    companion object {
        private const val DATABASE_NAME = "FlxDB"

        @Volatile
        private var instance: FlxDatabase? = null

        fun getInstance(context: Context): FlxDatabase = instance
            ?: synchronized(this) {
            instance
                ?: buildDatabase(
                    context
                ).also { instance = it }
        }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(context, FlxDatabase::class.java,
                DATABASE_NAME
            ).build()
    }
}