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
package com.flx.features.eula

import org.fuusio.api.feature.ViewFeature
import com.flx.app.R
import com.flx.features.eula.model.EulaViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

object EulaFeature : ViewFeature(R.id.nav_eula)  {

    private val module = module {
        viewModel { EulaViewModel() }
    }

    override fun getFeatureModule() =
        module
}