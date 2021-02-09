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

import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.widget.Button
import org.fuusio.api.view.ConfirmationBottomSheet
import com.flx.app.R
import com.flx.app.databinding.EulaAcceptanceFragmentBinding
import com.flx.app.view.MainActivity
import com.flx.features.eula.model.EulaViewModel
import org.koin.android.viewmodel.ext.android.viewModel
import kotlin.math.floor

class EulaAcceptanceFragment : StartFragment<EulaAcceptanceFragmentBinding>() {

    private val viewModel by viewModel<EulaViewModel>()

    private lateinit var acceptButton: Button
    private lateinit var declineButton: Button
    private lateinit var webView: WebView

    override fun createBinding(inflater: LayoutInflater, container: ViewGroup?): EulaAcceptanceFragmentBinding =
        EulaAcceptanceFragmentBinding.inflate(inflater, container, false)

    override fun onActivate(activity: StartActivity) {
        acceptButton = activity.positiveButton
        declineButton = activity.negativeButton

        declineButton.visibility = View.VISIBLE
        declineButton.text = getString(R.string.action_decline_eula)

        acceptButton.text = getString(R.string.action_accept_eula)
        acceptButton.isEnabled = false

        declineButton.setOnClickListener {
            confirmDisagreement()
        }

        acceptButton.setOnClickListener {
            viewModel.acceptEula()
            activity.finish()
        }
    }

    private fun confirmDisagreement(): Boolean {
        ConfirmationBottomSheet(
            this,
            R.string.title_exit_application,
            R.string.text_application_exit,
            R.string.action_cancel,
            R.string.action_ok
        ) { _, which ->
            if (which == DialogInterface.BUTTON_POSITIVE) {
                viewModel.declineEula()
                val activity = requireActivity()
                val intent = Intent()
                intent.putExtra(MainActivity.KEY_IS_EULA_DECLINED, true)
                activity.setResult(Activity.RESULT_OK, intent)
                activity.finish()
            }
        }.show()
        return true
    }

    override fun onSetupViewBindings(binding: EulaAcceptanceFragmentBinding) {
        webView = binding.webViewEula
        loadUrl("file:///android_asset/html/eula.html")

        webView.setOnScrollChangeListener { _, _, scrollY, _, _ ->
            val height = floor(webView.contentHeight * resources.displayMetrics.density - webView.height)
            val offset = (height - scrollY)

            if (offset < 8f) {
                acceptButton.isEnabled = true
            }
        }
    }

    @Suppress("SameParameterValue")
    private fun loadUrl(url: String) {
        webView.post { webView.loadUrl(url) }
    }
}
