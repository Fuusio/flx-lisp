/*
 * Copyright (C) 2001 - 2021 Marko Salmela
 *
 * http://fuusio.org
 *
 * All rights reserved.
 */
package com.flx.features.repl

import org.fuusio.api.feature.ViewFeature
import com.flx.lisp.app.R
import com.flx.features.repl.model.ReplViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

object ReplFeature : ViewFeature(R.id.nav_repl) {

    private val module = module { viewModel { ReplViewModel() } }

    override fun getFeatureModule() = module
}