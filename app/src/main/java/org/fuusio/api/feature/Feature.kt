package org.fuusio.api.feature

import androidx.annotation.CallSuper
import org.koin.core.context.loadKoinModules
import org.koin.core.context.unloadKoinModules
import org.koin.core.module.Module

abstract class Feature(val id:Int = hashCode()) {

    init {
        @Suppress("LeakingThis")
        registerFeature(this)
    }

    private var isActivated = false

    abstract fun getFeatureModule(): Module

    @CallSuper
    open fun activate() {
        if (!isActivated) {
            isActivated = true
            loadKoinModules(getFeatureModule())
        }
    }

    @CallSuper
    open fun deactivate() {
        if (isActivated) {
            isActivated = false
            unloadKoinModules(getFeatureModule())
        }
    }

    companion object {

        private val features = mutableMapOf<Int, Feature>()

        fun get(id: Int) = features[id]!!

        fun registerFeature(feature: Feature) {
            features[feature.id] = feature
        }
    }
}
