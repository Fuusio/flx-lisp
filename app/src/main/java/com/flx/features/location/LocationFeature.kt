package com.flx.features.location

import com.flx.features.location.logic.LocationManager
import org.fuusio.api.feature.Feature
import org.koin.dsl.module

object LocationFeature : Feature() {

    private val module = module {
        single { LocationManager(get()) }
    }

    override fun getFeatureModule() = module
}