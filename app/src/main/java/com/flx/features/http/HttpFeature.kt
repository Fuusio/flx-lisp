package com.flx.features.http

import com.flx.features.http.logic.HttpManager
import org.fuusio.api.feature.Feature
import org.koin.dsl.module

object HttpFeature : Feature() {

    private val module = module {
        single { HttpManager(get()) }
    }

    override fun getFeatureModule() = module
}