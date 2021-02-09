package com.flx.features.media

import org.fuusio.api.feature.Feature
import com.flx.features.media.logic.MediaManager
import org.koin.dsl.module

object MediaFeature : Feature() {

    private val module = module { single { MediaManager(get()) } }

    override fun getFeatureModule() = module
}