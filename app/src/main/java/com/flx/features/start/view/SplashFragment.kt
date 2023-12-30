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
package com.flx.features.start.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.flx.lisp.app.R
import com.flx.lisp.app.databinding.SplashFragmentBinding

class SplashFragment : StartFragment<SplashFragmentBinding>() {

    override fun createBinding(inflater: LayoutInflater, container: ViewGroup?): SplashFragmentBinding =
        SplashFragmentBinding.inflate(inflater, container, false)

    override fun onActivate(activity: StartActivity) {
        activity.negativeButton.visibility = View.GONE
        activity.positiveButton.text = getString(R.string.action_get_started)
        activity.positiveButton.setOnClickListener { activity.gotoNext() }
    }

    override fun onSetupViewBindings(binding: SplashFragmentBinding) {}
}
