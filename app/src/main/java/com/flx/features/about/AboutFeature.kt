package com.flx.features.about

import org.fuusio.api.feature.ViewFeature
import com.flx.lisp.app.R
import com.flx.features.about.model.AboutViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

object AboutFeature : ViewFeature(R.id.nav_about) {

    private val module = module { viewModel { AboutViewModel() } }

    override fun getFeatureModule() = module
}