package com.flx.features.eula.view

import android.content.DialogInterface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.widget.Button
import androidx.appcompat.widget.Toolbar
import androidx.navigation.fragment.findNavController
import org.fuusio.api.view.ConfirmationBottomSheet
import org.fuusio.api.view.ToolBarFragment
import com.flx.lisp.app.R
import com.flx.lisp.app.databinding.EulaFragmentBinding
import com.flx.features.eula.model.EulaViewModel
import org.koin.android.viewmodel.ext.android.viewModel
import kotlin.math.floor
import kotlin.system.exitProcess

class EulaFragment : ToolBarFragment<EulaFragmentBinding>() {

    private val viewModel by viewModel<EulaViewModel>()

    private lateinit var webView: WebView
    private lateinit var acceptButton: Button
    private lateinit var declineButton: Button

    override fun getToolBar() = binding.toolbar

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): EulaFragmentBinding =
        EulaFragmentBinding.inflate(inflater, container, false)

    override fun onSetupViewBindings(binding: EulaFragmentBinding) {
        acceptButton = binding.buttonAccept
        declineButton = binding.buttonDecline

        acceptButton.isEnabled = false
        declineButton.isEnabled = true

        acceptButton.setOnClickListener {
            viewModel.acceptEula()
            findNavController().navigateUp()
        }

        declineButton.setOnClickListener {
            confirmDisagreement()
        }

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

    override fun onResume() {
        super.onResume()

        if (viewModel.getUserInfo().eulaAccepted) {
            acceptButton.visibility = View.GONE
            declineButton.visibility = View.GONE
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
                exitProcess(0)
            }
        }.show()
        return true
    }

    override fun onSetupToolBar(toolBar: Toolbar) {
        toolBar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp)
    }
}
