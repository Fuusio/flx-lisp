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
package com.flx.features.about.view

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.ViewGroup
import android.webkit.WebView
import androidx.appcompat.widget.Toolbar
import org.fuusio.api.extension.observe
import org.fuusio.api.view.ToolBarFragment
import com.flx.lisp.app.R
import com.flx.lisp.app.databinding.AboutFragmentBinding
import com.flx.features.about.model.AboutViewModel
import org.koin.android.viewmodel.ext.android.viewModel

class AboutFragment : ToolBarFragment<AboutFragmentBinding>() {

    private val viewModel by viewModel<AboutViewModel>()

    override fun getToolBar() = binding.toolbar

    private lateinit var webView: WebView

    override fun getMenuRes() = R.menu.about_fragment_menu

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): AboutFragmentBinding
            = AboutFragmentBinding.inflate(inflater, container, false)

    @SuppressLint("SetJavaScriptEnabled")
    override fun onSetupViewBindings(binding: AboutFragmentBinding) {
        webView = binding.webViewOssLibraries
        webView.settings.javaScriptEnabled = true
        webView.post {  webView.loadUrl("file:///android_asset/html/oss_libraries.html") }

        observe(viewModel.versionText) { text -> binding.textViewVersion.text = text }
    }

    override fun onOptionsItemSelected(item: MenuItem) =
        when (item.itemId) {
            R.id.action_send_feedback -> doSendFeedback()
            R.id.action_goto_website -> doGotoWebsite()
            else -> super.onOptionsItemSelected(item)
        }

    private fun doGotoWebsite(): Boolean {
        val browserIntent = Intent(
            Intent.ACTION_VIEW,
            Uri.parse(WEB_ADDRESS)
        )
        startActivity(browserIntent)
        return true
    }

    private fun doSendFeedback(): Boolean {
        val emailIntent = Intent(
            Intent.ACTION_SENDTO, Uri.fromParts(
                MAIL_TO, EMAIL_ADDRESS, null
            )
        )
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Feedback about FLX app")
        emailIntent.putExtra(Intent.EXTRA_TEXT, "Your feedback ...")
        startActivity(Intent.createChooser(emailIntent, getString(R.string.text_send_email)))
        return true
    }

    override fun onSetupToolBar(toolBar: Toolbar) {
        toolBar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp)
    }

    companion object {
        private const val MAIL_TO = "mailto"
        private const val EMAIL_ADDRESS = "floxp.app@gmail.com"
        private const val WEB_ADDRESS = "http://floxp.app"
    }
}