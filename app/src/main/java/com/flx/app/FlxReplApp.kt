/*
 * Copyright (C) 2001 - 2021 Marko Salmela
 *
 * http://fuusio.org
 *
 * All rights reserved.
 */
package com.flx.app

import android.app.Application
import androidx.preference.PreferenceManager
import com.github.ajalt.timberkt.Timber
import com.flx.app.model.FlxAppStore
import com.flx.features.database.FlxDatabase
import com.flx.features.about.AboutFeature
import org.fuusio.api.util.ThemeUtils
import org.fuusio.flx.Flx
import com.flx.app.logic.UserInfoManager
import com.flx.features.eula.EulaFeature
import com.flx.features.location.LocationFeature
import com.flx.features.media.MediaFeature
import com.flx.features.repl.ReplFeature
import com.flx.features.repl.logic.ExecutionManager
import com.flx.features.start.StartFeature
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.fuusio.flx.core.vm.FlxCtx
import org.koin.android.ext.koin.androidContext
import org.koin.core.KoinApplication
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.dsl.module

class FlxReplApp : Application() {

    private lateinit var db: FlxDatabase
    private lateinit var executionManager: ExecutionManager
    private lateinit var koinApplication: KoinApplication
    private lateinit var store: FlxAppStore
    private lateinit var userInfoManager: UserInfoManager

    init {
        flxReplApp = this
    }

    @ExperimentalCoroutinesApi
    override fun onCreate() {
        super.onCreate()

        Flx.ctx = FlxCtx(this)

        Timber.plant(Timber.DebugTree())

        db = FlxDatabase.getInstance(this)

        store = FlxAppStore(db)
        executionManager = ExecutionManager()
        userInfoManager = UserInfoManager(store)

        val modules = getModules()

        koinApplication = startKoin {
            androidContext(this@FlxReplApp)
            modules(modules)
        }

        userInfoManager.getUserInfoFromDb()

        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        val themePref = sharedPreferences.getString("themePref", ThemeUtils.LIGHT_MODE)!!
        ThemeUtils.applyTheme(themePref)
    }

    private fun getModules(): List<Module> {
        val appModule = module {
            single { executionManager }
            single { db }
            single { store }
            single { userInfoManager }
        }

        return listOf(
            appModule,
            AboutFeature.getFeatureModule(),
            ReplFeature.getFeatureModule(),
            EulaFeature.getFeatureModule(),
            MediaFeature.getFeatureModule(),
            StartFeature.getFeatureModule(),
            LocationFeature.getFeatureModule()
        )
    }

    companion object {
        private var flxReplApp: FlxReplApp? = null
        val instance get() = flxReplApp!!

        fun getVersion(): String {
            return try {
                val context = instance.applicationContext
                val  packageInfo = context.packageManager.getPackageInfo(context.packageName, 0)
                packageInfo.versionName
            } catch (e : Exception) {
                "1.0.1"
            }
        }
    }
}

